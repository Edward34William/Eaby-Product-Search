# Eaby Product Search
PHP integrated web page, Angular7 based web application, and Android (adk28) Java application for Ebay product search.

__Lineng Cao__, _USC CSCI 571 2019SP_

## PHP Verison
- PHP7 combined with Html5
- All API called from PHP server side

## Angular Verison
- Entire Angular7 with Bootstrap powered FrontEnd website, Using reactive form and angular materials principles.(`@angular/ng-bootstrap`, `@angular/material`, `angular-svg-round-progressbar`).
- Nodejs & Express based BackEnd serving on AWS EC2 and Azure.
- Special Restful APIs were created for supporting all requests from the frontend filled with error handling.
- Featured with autocomplete, ip detected, group sorting and offline cart list.

## BackEnd Restful APIs
- [Zipcode IP Autocomplete](http://vvvvvincecccchw8.us-west-1.elasticbeanstalk.com/api/ip-json/?startsWith=900)
- [Google Image Search](http://vvvvvincecccchw8.us-west-1.elasticbeanstalk.com/api/google-img?v=1&productTitle=iphone)
- [eBay Product Search](http://vvvvvincecccchw8.us-west-1.elasticbeanstalk.com/api/search/?keyword=iphone&buyerPostalCode=90007&MaxDistance=100&FreeShippingOnly=true&LocalPickupOnly=true)
- [eBay Project Detail Search](http://vvvvvincecccchw8.us-west-1.elasticbeanstalk.com/api/item-detail/?itemId=283622107255)
- [eBay Similar Search](http://vvvvvincecccchw8.us-west-1.elasticbeanstalk.com/api/similar/?itemId=283622107255)

### Screenshots
__Detail__

![](./screenshots/angular/detail-opt.gif)

__Search__

![](./screenshots/angular/search-opt.gif)

__Wish List Feature__

![](./screenshots/angular/wish-opt.gif)

### Demo
[Demo Page](http://vvvvvincecccchw8.us-west-1.elasticbeanstalk.com/)

## Android Verison
- Nodejs & Express based BackEnd serving on aws and Azure.
- Using recyclerview, fragment and data module.
- Featured with autocomplete, ip detected, simliar group sorting, cache offline wish list

### Screenshots
__Launch__

![](./screenshots/android/launch-opt.gif)

__Detail__

![](./screenshots/android/detail-opt.gif)

__Search__

![](./screenshots/android/search-opt.gif)

![](./screenshots/android/search2-opt.gif)

__Wish List Feature__

![](./screenshots/android/wish_list-opt.gif)

### Download Demo
[Apk Download](https://gitlab.com/vcec/eaby-product-search/raw/master/AndroidVer/apk/product-search-debug-v1.apk)

