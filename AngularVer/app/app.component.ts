import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { JsonService } from './json.service';
import { DataService } from './data.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [JsonService]
})
export class AppComponent {

  constructor(private http: HttpClient, private jsonService: JsonService, private data: DataService) {
    this.showJson('https://ipapi.co/postal');
    
    if(localStorage.getItem('globalWishList') != null){
      this.data.globalWishList = JSON.parse(localStorage.getItem('globalWishList'));

      for (let index = 0; index < this.data.globalWishList.length; index++) {
        this.data.globalWishList[index].isSelect = false;
      }

    }

  }

  localzip;

  showJson(url) {
    this.localzip = ''
    this.jsonService.getJson(url)
      .subscribe((data) => {
        this.localzip = data;
        // console.log(data);
      }),
      error => {

      };
      
  }
  

  title = 'Product Search';
}
