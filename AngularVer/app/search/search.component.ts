import { Component, OnInit, Input, ElementRef, Renderer2, ViewChild } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { JsonService } from '../json.service';
import { DataService } from '../data.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
  providers: [JsonService]
})
export class SearchComponent implements OnInit {

  @Input()
  localZip: string;
  submitted = false;
  getIp = false;
  model: any = {};
   
  constructor(private http: HttpClient, private jsonService: JsonService, private data: DataService, private renderer: Renderer2) {
  }

  genForm(local): void {
    this.form = new FormGroup({
      keyword: new FormControl('', Validators.compose([
        Validators.required,
        this.keywordValidator
      ])),
      category: new FormControl('-1'),
      condition: new FormGroup({
        new: new FormControl(false),
        used: new FormControl(false),
        unspecified: new FormControl(false)
      }),
      shipping: new FormGroup({
        localpickup: new FormControl(false),
        freeshipping: new FormControl(false)
      }),
      distance: new FormControl(null),
      InputLocationRadios: new FormControl('current'),
      local_zipcode: new FormControl(local, Validators.required),
      cus_zipcode: new FormControl({ value: '', disabled: true }, Validators.compose([
        Validators.required,
        this.zipcodeValidator
      ])),
    });
  }

  form;
  options: string[] = [];
  ngOnInit() {
    this.genForm(this.localZip);
    
    let timerLocalZip = setInterval(() => {
      // console.log('try setting zip');
      this.form.controls['local_zipcode'].setValue(this.localZip)
      if(this.form.controls['local_zipcode'].value != ''){
        clearInterval(timerLocalZip);
        // console.log('interval cancel');
      }
    }, 1000);
    setTimeout(() => {
      clearInterval(timerLocalZip);
      // console.log('time out');
    }, 60000);
    
  }

  showAutoCompleteJson(url) {
    this.options = [];
    this.jsonService.getJson(url)
      .subscribe(data => {
        this.options.push(...data);
        // console.log(this.options);
      }),
      error => {
        
      };
  }

  zipcodeValidator(control) {
    let str = control.value.trim();
    let regex = RegExp('^\\d{5}$');
    if (!regex.test(str)) {
      return { 'cus_zipcode': true };
    }
    return null;
  }

  keywordValidator(control) {
    let str = control.value.trim();
    let regex = RegExp('[\\S]+');
    if (!regex.test(str)) {
      return { 'cus_zipcode': true };
    }
    return null;
  }

  onSubmit(searchForm) {

    this.data.isSubmit = false;
    this.data.fetchLocalStorageSearchResult = false;
    this.data.listTabIndex == 0;

    setTimeout(()=>{
      let element: HTMLElement = document.getElementById('tab-0') as HTMLElement;
      element.click();

      let InputDistance: HTMLInputElement = document.getElementById('InputDistance') as HTMLInputElement;

      if (InputDistance.value == "") {
        this.form.controls['distance'].setValue('10');
        InputDistance.value = '';
      }

      this.submitted = true;
      // console.log(searchForm.value);
      this.data.formInfoJson = searchForm.value;
      this.data.isSubmit = true;
      this.data.fetchLocalStorageSearchResult = false;

    }, 1000);
  }

  onReset() {
    this.onCurrent();
    this.ngOnInit();
    this.options = [];

    this.data.isSubmit = false;
    this.data.fetchLocalStorageSearchResult = false;
    
    let element: HTMLElement = document.getElementById('tab-0') as HTMLElement;
    element.click();

    for (let index = 0; index < this.data.globalWishList.length; index++) {
      this.data.globalWishList[index]['isSelect'] = false;
    }

  }

  onCurrent() {
    this.form.controls['cus_zipcode'].markAsUntouched();
    this.form.controls['cus_zipcode'].disable();

    let InputZipcode: HTMLInputElement = document.getElementById('InputZipcode') as HTMLInputElement;
    InputZipcode.value = '';
  }

  onCustom() {
    this.form.controls['cus_zipcode'].setValue('');
    this.form.controls['cus_zipcode'].enable();
    this.options = [];
  }

  isDisabled() {
    return (this.form.invalid);
  }

  onKeyWordDown() {
    this.form.controls['keyword'].markAsTouched();
  }

  onHover() {
    this.form.controls['local_zipcode'].setValue(this.localZip);
  }

  onZipcodeDown() {
    this.form.controls['cus_zipcode'].markAsTouched();
  }

  onZipcodeUp(event) {
    try {
      if (event.target.value) {
        this.options = [];
        this.showAutoCompleteJson( this.data.domain + 'api/ip-json/?startsWith=' + event.target.value);
        // console.log(event.target.value);
      }
    } catch{
      
    }
    
    
  }
}
