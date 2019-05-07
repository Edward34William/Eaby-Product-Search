import { Component, OnInit } from '@angular/core';
import { trigger, state, transition, animate, style } from '@angular/animations';
import { JsonService } from 'src/app/json.service';
import { DataService } from 'src/app/data.service';

@Component({
  selector: 'app-wish-detail',
  templateUrl: './wish-detail.component.html',
  styleUrls: ['../detail/detail.component.css'],
  animations: [
    trigger('fade', [
      state('void', style({ opacity: 0 })),
      transition('void <=> *', [
        animate(150)
      ])
    ])
  ]
})
export class WishDetailComponent implements OnInit {

  constructor(private jsonService: JsonService, public data: DataService) { }

  tabs = [{
    index: 0,
    name: "Product",
    isActive: false
  }, {
    index: 1,
    name: "Photos",
    isActive: false
  }, {
    index: 2,
    name: "Shopping",
    isActive: false
  }, {
    index: 3,
    name: "Seller",
    isActive: false
  }, {
    index: 4,
    name: "Similar Products",
    isActive: false
  }]

  currentTabIndex = 0;
  lessMode = true;
  sortMode = "asc";
  isASCDisabled = true;
  isWish = true;
  detail = {};
  isLoad = true;

  localSimilar;
  photos: string[] = [];
  similars: string[] = [];
  quote: string;
  href: string;

  ngOnInit() {
    
    if(window.innerWidth < 1000) this.tabs[4]['name'] = 'Related';

    if(!this.data.fetchLocalWishStorageDetail){
      this.data.fetchLocalWishStorageDetail = true;
      if(this.data.globalWishItemId == -1){
        let element: HTMLElement = document.getElementById('tab-0') as HTMLElement;
        element.click();
      }else{
        this.showItemDetail(this.data.domain + 'api/item-detail/?itemId=' + encodeURI(this.data.globalWishItemId));
      }
      
    }else{
      this.detail = JSON.parse(localStorage.getItem('wish-detail'));
      this.toogleTab(0);
      this.photos = JSON.parse(localStorage.getItem('wish-photos'));
      this.similars = JSON.parse(localStorage.getItem('wish-similars'));
      this.localSimilar = this.similars.slice();
      this.genFB();
      this.isLoad = false;
    }


  }

  genFB(){
    let quote = 'Buy ' + this.detail['title'] + ' at ' + this.detail['product'][2].value + ' from link below.';
    this.href = 'https://www.facebook.com/dialog/share?app_id=377233376198486&display=popup&href='+ this.detail['viewItemURLForNaturalSearch'] +'&redirect_uri=' + encodeURI('http://localhost') +'&quote='+encodeURI(quote);
    //console.log(this.href); 
  }

  showGooglePhotos(pre, version, title) {
    
    let url = pre + '?v=' + version + '&productTitle=' + title;
    // console.log('google api', url);
    this.jsonService.getJson(url)
      .subscribe(data => {
        this.photos = [];
        this.photos.push(...data);
        if(data.length == 0 && version == 1){
          // console.log('swtiching google image');
          this.showGooglePhotos(this.data.domain + 'api/google-img/', 2, title);
        }
        localStorage.setItem('wish-photos', JSON.stringify(this.photos));
        // console.log('wish-photos', this.photos);
      });
  }

  showItemDetail(url) {
    // console.log('detail api', url);
    this.jsonService.getJson(url)
      .subscribe(data => {
        this.detail = data;
        localStorage.setItem('wish-detail', JSON.stringify(this.detail));
        this.toogleTab(0);
        let title = data['title'].trim();
        // this.showGooglePhotos(this.data.domain + 'api/google-img/', 1, encodeURI(data['title'].trim().split(' ', 4).join(' ')));
        this.showGooglePhotos(this.data.domain + 'api/google-img/', 1, encodeURI(data['title'].trim().match(/\w+/g).slice(0,5).join(' ')));
        this.showSimilar(this.data.domain + 'api/similar/?itemId=' + encodeURI(data['itemId']));
        this.genFB();
        this.isLoad = false;
      });
  }

  showSimilar(url) {
    // console.log('sim api', url);
    this.jsonService.getJson(url)
      .subscribe(data => {
        this.similars = [];
        this.similars.push(...data);
        localStorage.setItem('wish-similars', JSON.stringify(this.similars));
        this.localSimilar = this.similars.slice();
        // console.log('wish-sim', this.similars);
      });
  }

  toogleTab(tabIndex) {
    this.tabs[this.currentTabIndex].isActive = false;
    this.currentTabIndex = tabIndex;
    this.tabs[this.currentTabIndex].isActive = true;
  }

  typeOf(value) {
    return typeof (value);
  }

  onClickSort(key) {
    if (key == 'default') {
      this.localSimilar = this.similars.slice();
      this.isASCDisabled = true;
      return this.localSimilar;
    };
    this.isASCDisabled = false;

    let element: HTMLInputElement = document.getElementById('category-sort') as HTMLInputElement;
    if (key == 'asc') { 
      this.sortMode = "asc"; 
      this.onClickSort(element.value);
    };
    if (key == 'des') { 
      this.sortMode = "des"; 
      this.onClickSort(element.value);
    };

    if (this.sortMode == "asc") {
      this.localSimilar.sort((a, b) => a[key] > b[key] ? 1 : a[key] < b[key] ? -1 : 0);
    } else {
      this.localSimilar.sort((a, b) => a[key] < b[key] ? 1 : a[key] > b[key] ? -1 : 0);
    }
    return this.localSimilar;
  }

  wishClick(isAlready) {
    if (isAlready) { //remove
      this.data.removeWishList(this.detail['itemId'])
    } else { //add
      this.data.globalWishList = this.data.WishBackUp;
      // console.log(this.data.WishBackUp);
    }
  }

  checkIfinWish(){
    for (let index = 0; index < this.data.globalWishList.length; index++) {
      if (this.data.globalWishList[index]['itemId'] == this.detail['itemId']){
        return true;
      };
    }
    return false;
  }

}
