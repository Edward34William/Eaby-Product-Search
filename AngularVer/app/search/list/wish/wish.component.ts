import { Component, OnInit } from '@angular/core';
import { DataService } from 'src/app/data.service';
import { ResultComponent } from '../result/result.component';

@Component({
  selector: 'app-wish',
  templateUrl: './wish.component.html',
  styleUrls: ['./wish.component.css']
})
export class WishComponent implements OnInit {

  constructor(public data: DataService) { }
  hasNoWishRecord = false;
  headers = ['#', 'Image', 'Title', 'Price', 'Shipping', 'Seller', 'Wish List'];
  currectActItem = -1;
  ngOnInit() {

  }

  isDetail() {
    return true;
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

  loadDetail(sample) {
    // itemId = '311899027613';
    this.data.globalWishItemId = sample.itemId;
    this.data.globalWishDetailInfo = sample.detail;
    this.data.fetchLocalWishStorageDetail = false;
  }


  clickTitle(sample, index) {
    if (sample.itemId != this.data.globalWishItemId && !sample.isSelect) {
      this.loadDetail(sample);
      this.data.resultId = sample.id;
    }
    this.itemClick(index);
  }


  itemClick(i) {
    
    // this.data.isWishDetailEnable = true;

    for (let index = 0; index < this.data.globalWishList.length; index++) {
      this.data.globalWishList[index]['isSelect'] = false;
    }

    this.data.globalWishList[i]['isSelect'] = true;
  }

  getTotal(){
    let sum = 0;
    for (let index = 0; index < this.data.globalWishList.length; index++) {
      sum += parseFloat(this.data.globalWishList[index]['price']);
    }
    return sum.toFixed(2);
  }

  isDisable(){
    for (let index = 0; index < this.data.globalWishList.length; index++) {
      if(this.data.globalWishList[index]['isSelect']){
        return false;
      }
    }
    return true;
    
  }

}
