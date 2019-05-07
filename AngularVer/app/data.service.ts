import { Injectable, OnInit } from '@angular/core';
import { getMatIconFailedToSanitizeUrlError } from '@angular/material';

@Injectable({
  providedIn: 'root'
})
export class DataService implements OnInit {
  fetchLocalStorageSearchResult: any = false;

  fetchLocalProductStorageDetail: any = false;
  fetchLocalWishStorageDetail: any = false;

  globalProductItemId: any = '-1';
  globalWishItemId: any = '-1';

  globalProductDetailInfo: any = {};
  globalWishDetailInfo: any = {};
  
  globalWishList: any = [];

  WishBackUp: any = [];
  sampleList: any = [];
  resultId: any = -1;

  isSubmit: any = false;
  formInfoJson: any = {};

  ngOnInit(): void {
    
  }

  constructor() { }
  listTabIndex: any;
  itemID: any;
  domain: any = 'http://localhost:8080/'; //backend RESTApi url, Modified required

  updateWishList(search){
    let obj = JSON.parse(search);
    delete obj.isWish;
    this.globalWishList.push(obj);
    localStorage.setItem('globalWishList', JSON.stringify(this.globalWishList));
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
  }


}
