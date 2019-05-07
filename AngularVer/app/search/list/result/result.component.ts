import { Component, OnInit, Input } from '@angular/core';
import { DataService } from 'src/app/data.service';
import { JsonService } from 'src/app/json.service';

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent implements OnInit {

  constructor(private jsonService: JsonService, private data: DataService) { }
  isFirst = true;
  isLast = false;
  hasRecord = true; //error handle
  headers = ['#', 'Image', 'Title', 'Price', 'Shipping', 'Zip', 'Seller', 'Wish List'];

  lastIndex = 0;
  ItemPrePage = 10;
  pages = [];

  currectActPage: any = 1;

  isLoad = true;
  detailList = [];
  ngOnInit() {

    if (!this.data.fetchLocalStorageSearchResult) {
      this.data.fetchLocalStorageSearchResult = true;
      this.showSearchResult(this.data.domain + this.formatFormInfo(this.data.formInfoJson));
      localStorage.setItem('currectActPage', JSON.stringify(1));
      this.data.globalProductItemId = '-1';

    } else {

      this.initWithLocalWish();

      this.getPageNum();
      this.checkIfLast();
      this.pages[0].active = true;
      this.jumpPage(JSON.parse(localStorage.getItem("currectActPage")));
      this.isLoad = false;
    }

    // this.detailList = this.data.sampleList;
  }

  formatFormInfo(obj){
    let keyword = obj.keyword;
    let buyerPostalCode = (obj.InputLocationRadios == 'customized')?obj.cus_zipcode:obj.local_zipcode;
    let MaxDistance = obj.distance;
    let FreeShippingOnly = obj.shipping.freeshipping;
    let LocalPickupOnly = obj.shipping.localpickup;
    let Condition = (obj.condition.new || obj.condition.unspecified || obj.condition.used);
    let c_new = obj.condition.new, c_used = obj.condition.used, c_unspecified = obj.condition.unspecified;
    let categoryId = obj.category;

    let res =  'api/search/?keyword=' + keyword
    + ((buyerPostalCode)?('&buyerPostalCode='+buyerPostalCode):'')
    + ((MaxDistance)?('&MaxDistance='+MaxDistance):'')
    + ((FreeShippingOnly)?('&FreeShippingOnly='+FreeShippingOnly):'')
    + ((LocalPickupOnly)?('&LocalPickupOnly='+LocalPickupOnly):'')
    + ((Condition)?('&Condition=true' + ((c_new)?('&New='+c_new):'') + ((c_used)?('&Used='+c_used):'') + ((c_unspecified)?('&Unspecified='+c_unspecified):'')):'')
    + ((categoryId != -1)?('&categoryId='+categoryId):'');

    // console.log(res);

    return res;

  }

  showSearchResult(url) {
    // console.log('search api', url);
    this.jsonService.getJson(url)
      .subscribe(data => {
        this.data.sampleList = [];
        this.data.sampleList.push(...data);

        this.initWithLocalWish();

        this.getPageNum();
        this.pages[0].active = true;
        this.checkIfLast();
        this.isLoad = false;
      });
  }

  prePage(n) {
    this.lastIndex -= this.ItemPrePage * n;
    this.activePage(this.currectActPage - n);
    this.checkIfLast();
    this.checkIfFirst();

  }

  nexPage(n) {
    this.lastIndex += this.ItemPrePage * n;
    this.activePage(this.currectActPage + n);
    this.checkIfLast();
    this.checkIfFirst();
  }

  jumpPage(newPage) {
    if (newPage < this.currectActPage) {
      this.prePage(this.currectActPage - newPage);
    } else {
      this.nexPage(newPage - this.currectActPage);
    }
  }

  checkIfLast() {
    this.isLast = (this.pages[this.pages.length - 1].active == true);
  }

  checkIfFirst() {
    this.isFirst = (this.pages[0].active == true);
  }

  titleProcess(title) {
    if (title.length > 34) {
      let n = title.substring(0, 34).lastIndexOf(" ");
      if (n >= 0) {
        let str = title.substring(0, n);
        str += " ...";
        return str;
      }
      if(n == -1){
        return '...'
      }
    }
    return title;
  }

  getPageNum() {
    let pageNum;
    if(this.data.sampleList.length == 0){
      pageNum = 1
    }else{
      pageNum = Math.ceil(this.data.sampleList.length / this.ItemPrePage);
    }
    for (let index = 1; index <= pageNum; index++) {
      this.pages.push({
        page: index,
        active: false
      })

    }
  }

  activePage(page) {
    this.pages[this.currectActPage - 1].active = false;
    this.currectActPage = page;
    localStorage.setItem('currectActPage', JSON.stringify(page));
    this.pages[this.currectActPage - 1].active = true;
  }

  // table
  itemClick(i) {
    // this.data.isProductDetailEnable = true;

    for (let index = 0; index < this.data.sampleList.length; index++) {
      this.data.sampleList[index].isSelect = false;
    }

    this.data.sampleList[i].isSelect = true;
  }

  wishClick(id, itemId) {
    if (this.data.sampleList[id - 1].isWish) { //remove
      this.data.removeWishList(itemId)
    } else { //add
      let search = this.data.sampleList[id - 1];
      // console.log(JSON.stringify(search));
      this.data.updateWishList(JSON.stringify(search).replace('"isSelect":true', '"isSelect":false'));

    }
    this.data.sampleList[id - 1].isWish = !this.data.sampleList[id - 1].isWish;

  }

  isDetail() {
    return true;
  }

  loadDetail(sample) {
    this.data.globalProductItemId = sample.itemId;
    this.data.globalProductDetailInfo = sample.detail;

    // console.log(sample.itemId);
    // console.log(sample.id - 1);
    this.data.fetchLocalProductStorageDetail = false;
  }

  clickTitle(sample) {
    if (sample.itemId != this.data.globalProductItemId && !sample.isSelect) {
      this.loadDetail(sample);
      this.data.resultId = sample.id;
    }
    this.itemClick(sample.id - 1);
  }

  initWithLocalWish() {
    for (let i = 0; i < this.data.sampleList.length; i++) {
      this.data.sampleList[i].isWish = false;
      for (let index = 0; index < this.data.globalWishList.length; index++) {
        if (this.data.globalWishList[index].itemId == this.data.sampleList[i].itemId) {
          this.data.sampleList[i].isWish = true;
        }

      }

    }
  }

  isDisable(){
    for (let index = 0; index < this.data.sampleList.length; index++) {
      if(this.data.sampleList[index]['isSelect']){
        return false;
      }
    }
    return true;
  }

}
