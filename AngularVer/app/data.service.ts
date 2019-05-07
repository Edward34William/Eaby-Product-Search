import { Injectable, OnInit } from '@angular/core';
import { getMatIconFailedToSanitizeUrlError } from '@angular/material';

@Injectable({
  providedIn: 'root'
})
export class DataService implements OnInit {
  fetchLocalStorageSearchResult: any = false;

  fetchLocalProductStorageDetail: any = false;
  fetchLocalWishStorageDetail: any = false;

  // globalItemId: any = '-1';
  globalProductItemId: any = '-1';
  globalWishItemId: any = '-1';

  // globalDetailInfo: any = {};
  globalProductDetailInfo: any = {};
  globalWishDetailInfo: any = {};
  
  globalWishList: any = [];

  WishBackUp: any = [];
  sampleList: any = [];
  resultId: any = -1;

  isSubmit: any = false;
  formInfoJson: any = {};

  // isProductDetailEnable: any = false;
  // isWishDetailEnable: any = false;

  ngOnInit(): void {
    
  }

  constructor() { }
  listTabIndex: any;
  itemID: any;
  domain: any = 'http://vvvvvincecccchw8.us-west-1.elasticbeanstalk.com/';
  // domain: any = 'http://localhost:8080/';

  updateWishList(search){
    let obj = JSON.parse(search);
    delete obj.isWish;
    this.globalWishList.push(obj);
    localStorage.setItem('globalWishList', JSON.stringify(this.globalWishList));
    // console.log(this.globalWishList);
  }

  removeWishList(itemId){
    this.WishBackUp = this.globalWishList.slice();
    let newlst = [];

    this.globalWishList.forEach(element => {
      if(element['itemId'] != itemId){
        newlst.push(element);
      }
    });
    this.globalWishList = newlst;
    localStorage.setItem('globalWishList', JSON.stringify(this.globalWishList));
    // console.log(this.globalWishList);
  }


}
