const express = require('express');
const http = require('http');
const path = require('path');
const request = require('request');
const bp = require('body-parser');
const router = express.Router();

const app = express();

const cors = require('cors')

var corsOptions = {
  origin: '*',
  optionsSuccessStatus: 200
}
app.use(cors(corsOptions))
app.use(express.static(__dirname + '/dist/search-product'));
app.use(bp.urlencoded({
  extended: true
}));
app.use(bp.json());
app.use('/api', router);

app.use(function (res) {
  res.status(404).send("404");
})
app.get('/*', (req, res) => res.sendFile(path.join(__dirname)));

// *********************************** router for angular

router.get('/ip-json/', function (req, res) {
  if (!req.query.startsWith || req.query.startsWith == "") {
    return res.status(400).send({
      success: 'false',
      message: 'startsWith is required'
    });
  } else {
    let startsWith = req.query.startsWith;
    let url = 'http://api.geonames.org/postalCodeSearchJSON?postalcode_startsWith=' + startsWith + '&username=vvcc&country=US&maxRows=5';
    getJsonFromUrl(url, function (obj) {
      let out = []
      obj['postalCodes'].forEach(element => {
        out.push(element['postalCode'])
      });
      res.json(out);
      console.log("startsWith", startsWith);
    });
  }
});

router.get('/google-img/', function (req, res) {
  if (!req.query.productTitle || req.query.productTitle == "") {
    return res.status(400).send({
      success: 'false',
      message: 'productTitle is required'
    });
  } else {
    let productTitle = req.query.productTitle;
    
    //use your own key
    let googleKEY2 = 'AIzaXXXXXXXXXXX';
    let googleKEY1 = 'AIzaXXXXXXXXXXX';
    let cx2 = '0162XXXXXXXXXX';
    let cx1 = '017XXXXXXXXXXX';
    
    let url;
    if (req.query.v == '1') {
      url = 'https://www.googleapis.com/customsearch/v1?q=' + encodeURI(productTitle) + '&cx=' + cx1 + '&imgSize=huge&imgType=news&num=8&searchType=image&key=' + googleKEY1;

    } else if (req.query.v == '2') {
      url = 'https://www.googleapis.com/customsearch/v1?q=' + encodeURI(productTitle) + '&cx=' + cx2 + '&imgSize=huge&imgType=news&num=8&searchType=image&key=' + googleKEY2;
    }
    // let url = 'http://127.0.0.1:5500/src/app/json/google.json';
    getJsonFromUrl(url, function (obj) {
      console.log('google url', url);
      let out = []
      if (obj['items']) {
        obj['items'].forEach(element => {
          out.push(element['link'])
        });
      }
      res.json(out);
      console.log("productTitle", productTitle);
    });
  }
});

router.get('/similar/', function (req, res) {
  if (!req.query.itemId || req.query.itemId == "") {
    return res.status(400).send({
      success: 'false',
      message: 'itemId is required'
    });
  } else {
    let itemId = req.query.itemId;
    
    //use your own key
    let url = 'http://svcs.ebay.com/MerchandisingService?OPERATION-NAME=getSimilarItems&SERVICE-NAME=MerchandisingService&SERVICE-VERSION=1.1.0&CONSUMER-ID=LinengCa-XXXXXXXXXXXXXX&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&itemId=' + itemId + '&maxResults=20'; 
    
    // let url = 'http://127.0.0.1:5500/src/app/json/ebay1.json';
    getJsonFromUrlGet(url, function (obj) {
      console.log('similar url', url);
      let out = [];
      obj['getSimilarItemsResponse']['itemRecommendations']['item'].forEach(element => {
        out.push({
          _img: element['imageURL'],
          _title: element['title'],
          _viewItemURL: element['viewItemURL'],
          _buyItNowPrice: (parseFloat(element['buyItNowPrice']['__value__']) == 0 && parseFloat(element['currentPrice']['__value__']) != 0) ? parseFloat(element['currentPrice']['__value__']) : parseFloat(element['buyItNowPrice']['__value__']),
          _shippingCost: parseFloat(element['shippingCost']['__value__']),
          _timeLeft: parseFloat(timeLeftTrim(element['timeLeft'], 'P', 'D')),
        })
      });
      res.json(out);
      console.log("similar api", itemId);

    });
  }
});

router.get('/item-detail/', function (req, res) {
  if (!req.query.itemId || req.query.itemId == "" || req.query.itemId == '-1') {
    return res.status(400).send({
      success: 'false',
      message: 'productTitle is required'
    });
  } else {
    let itemId = req.query.itemId;

    //use your own key
    let url = 'http://open.api.ebay.com/shopping?callname=GetSingleItem&responseencoding=JSON&appid=LinengCa-vvvvince-PRD-160cXXXXXXXXXXXXXX&siteid=0&version=967&ItemID=' + itemId + '&IncludeSelector=Description,Details,ItemSpecifics';
    // let url = 'http://127.0.0.1:5500/src/app/json/ebay2.json';
    getJsonFromUrlGet(url, function (obj) {
      console.log('detail url', url);

      if (obj['Ack'] != 'Failure') {
        let out = {}
        out['itemId'] = (obj['Item'] && obj['Item']['ItemID'])?(obj['Item']['ItemID']):null;
        out['title'] = (obj['Item'] && obj['Item']['Title'])?obj['Item']['Title']:null;
        out['viewItemURLForNaturalSearch'] = (obj['Item'] && obj['Item']['ViewItemURLForNaturalSearch'])?(obj['Item']['ViewItemURLForNaturalSearch']):null;

        //product
        out['product'] = [];
        (obj['Item'] && obj['Item']['PictureURL']) ? out['product'].push({
          key: "Product Images",
          photos: obj['Item']['PictureURL']
        }) : null;
        (obj['Item'] && obj['Item']['Subtitle']) ? out['product'].push({
          key: "Subtitle",
          value: obj['Item']['Subtitle']
        }) : null;
        (obj['Item'] && obj['Item']['CurrentPrice'] && obj['Item']['CurrentPrice']['Value']) ? out['product'].push({
          key: "Price",
          value: '$' + parseFloat(obj['Item']['CurrentPrice']['Value']).toFixed(2)
        }) : null;
        (obj['Item'] && obj['Item']['Location']) ? out['product'].push({
          key: "Location",
          value: obj['Item']['Location']
        }) : null;
        (obj['Item'] && obj['Item']['ReturnPolicy'] && obj['Item']['ReturnPolicy']['ReturnsAccepted'])  ? out['product'].push({
          key: "Return Policy",
          value: (obj['Item']['ReturnPolicy']['ReturnsWithin']) ? obj['Item']['ReturnPolicy']['ReturnsAccepted'] + ' Within ' + obj['Item']['ReturnPolicy']['ReturnsWithin'] : 'Returns Not Accepted'
        }) : null;
        if (obj['Item'] && obj['Item']['ItemSpecifics'] && obj['Item']['ItemSpecifics']['NameValueList'].length > 0) {
          obj['Item']['ItemSpecifics']['NameValueList'].forEach(element => {
            out['product'].push({
              key: element['Name'],
              value: element['Value'].join(', ')
            })
          });
        }

        res.json(out);
        console.log("detail api", itemId);
      }
    });
  }
});


router.get('/search/', function (req, res) {
  if (!req.query.keyword || req.query.keyword == "") {
    return res.status(400).send({
      success: 'false',
      message: 'keyword is required'
    });
  } else {
    let keyword = req.query.keyword;
    let buyerPostalCode = req.query.buyerPostalCode;
    let count = 0;
    let count2 = 0;

    //use your own key
    let url = 'http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0' +
      '&SECURITY-APPNAME=LinengCa-vvvvince-PRD-XXXXXXXXXXXXXXX' +
      '&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&paginationInput.entriesPerPage=50' +
      '&keywords=' + keyword + '&buyerPostalCode=' + buyerPostalCode +
      ((req.query.MaxDistance) ? ('&itemFilter(' + count++ + ').name=MaxDistance&itemFilter(' + (count - 1) + ').value=' + req.query.MaxDistance) : '') +
      ((req.query.FreeShippingOnly) ? ('&itemFilter(' + count++ + ').name=FreeShippingOnly&itemFilter(' + (count - 1) + ').value=' + req.query.FreeShippingOnly) : '') +
      ((req.query.LocalPickupOnly) ? ('&itemFilter(' + count++ + ').name=LocalPickupOnly&itemFilter(' + (count - 1) + ').value=' + req.query.LocalPickupOnly) : '') +
      '&itemFilter(' + count++ + ').name=HideDuplicateItems&itemFilter(' + (count - 1) + ').value=true' +
      ((req.query.Condition) ? ('&itemFilter(' + count++ + ').name=Condition') : '') +
      ((req.query.New) ? ('&itemFilter(' + (count - 1) + ').value(' + count2++ + ')=New') : '') +
      ((req.query.Used) ? ('&itemFilter(' + (count - 1) + ').value(' + count2++ + ')=Used') : '') +
      ((req.query.Unspecified) ? ('&itemFilter(' + (count - 1) + ').value(' + count2++ + ')=Unspecified') : '') +
      ((req.query.categoryId) ? ('&categoryId=' + (req.query.categoryId)) : '') +
      '&outputSelector(0)=SellerInfo&outputSelector(1)=StoreInfo';
    // let url = 'http://127.0.0.1:5500/src/app/json/ebay3.json';

    getJsonFromUrlGet(url, function (obj) {
      let out = [];
      let id = 1;
      try {
        (obj['findItemsAdvancedResponse'] && obj['findItemsAdvancedResponse'][0]['searchResult'] && obj['findItemsAdvancedResponse'][0]['searchResult'][0]['item'].length>0) ? obj['findItemsAdvancedResponse'][0]['searchResult'][0]['item'].forEach(element => {
          out.push({
            id: id++,
            image: element['galleryURL'] ? element['galleryURL'][0] : null,
            title: element['title'] ? element['title'][0] : null,
            price: (element['sellingStatus'] && element['sellingStatus'][0]['currentPrice'] && element['sellingStatus'][0]['currentPrice'][0]['__value__']) ? parseFloat(element['sellingStatus'][0]['currentPrice'][0]['__value__']).toFixed(2) : null,
            shipping: parseFloat(element['shippingInfo'][0]['shippingServiceCost'][0]['__value__']).toFixed(2),
            zip: (element['postalCode']) ? (element['postalCode'][0]) : null,
            seller: (element['sellerInfo'] && element['sellerInfo'][0]['sellerUserName']) ? (element['sellerInfo'][0]['sellerUserName'][0]) : null,
            isWish: false,
            isSelect: false,
            itemId: (element['itemId']) ? (element['itemId'][0]) : null,
            //for shipping
            detail: {
              shipping: [
                (element['shippingInfo'] && element['shippingInfo'][0]['shippingServiceCost'] && element['shippingInfo'][0]['shippingServiceCost'][0]['__value__'])?{
                  key: "Shipping Cost",
                  value: parseFloat(element['shippingInfo'][0]['shippingServiceCost'][0]['__value__']).toFixed(2),
                }:null,
                (element['shippingInfo'] && element['shippingInfo'][0]['shipToLocations'])?{
                  key: "Shipping Locations",
                  value: element['shippingInfo'][0]['shipToLocations'][0]
                }:null,
                (element['shippingInfo'] && element['shippingInfo'][0]['handlingTime']) ? {
                  key: "Handling Time",
                  value: element['shippingInfo'][0]['handlingTime'][0]
                } : null,
                (element['shippingInfo'] && element['shippingInfo'][0]['expeditedShipping'])?{
                  key: "Expedited Shipping",
                  value: (element['shippingInfo'][0]['expeditedShipping'][0] === 'true')
                }:null,
                (element['shippingInfo'] && element['shippingInfo'][0]['oneDayShippingAvailable'])?{
                  key: "One Day Shipping",
                  value: (element['shippingInfo'][0]['oneDayShippingAvailable'][0] === 'true')
                }:null,
                (element['returnsAccepted'])?{
                  key: "Return Accepted",
                  value: (element['returnsAccepted'][0] === 'true')
                }:null
              ],
              seller: {
                name: (element['sellerInfo'] && element['sellerInfo'][0]['sellerUserName'])?(element['sellerInfo'][0]['sellerUserName'][0]):null,
                table: [
                  (element['sellerInfo'] && element['sellerInfo'][0]['feedbackScore'])?{
                    key: "Feedback Score",
                    value: parseFloat(element['sellerInfo'][0]['feedbackScore'][0])
                  }:null,
                  (element['sellerInfo'] && element['sellerInfo'][0]['positiveFeedbackPercent'])?{
                    key: "Popularity",
                    value: parseFloat(element['sellerInfo'][0]['positiveFeedbackPercent'][0])
                  }:null,
                  (element['sellerInfo'] && element['sellerInfo'][0]['feedbackRatingStar'])?{
                    key: "Feedback Raing Star",
                    value: element['sellerInfo'][0]['feedbackRatingStar'][0]
                  }: null,
                  (element['sellerInfo'] && element['sellerInfo'][0]['topRatedSeller']) ? {
                    key: "Top Rated",
                    value: (element['sellerInfo'][0]['topRatedSeller'][0] === 'true')
                  } : null,
                  (element['storeInfo'] && element['storeInfo'][0]['storeName']) ? {
                    key: "Store Name",
                    value: element['storeInfo'][0]['storeName'][0]
                  } : null,
                  (element['storeInfo'] && element['storeInfo'][0]['storeURL']) ? {
                    key: "Buy Product At",
                    value: element['storeInfo'][0]['storeURL'][0]
                  } : null
                ]
              }
            }

          })
        }) : null;
        let a = out[0]['detail']['shipping'].filter(function(e){return e});
        out[0]['detail']['shipping'] = a;
        let b = out[0]['detail']['seller']['table'].filter(function(e){return e});
        out[0]['detail']['seller']['table'] = b;
        console.log(out);
        res.json(out);
        console.log("search api", url);
      }catch(e){
        res.json([]);
      }
    });
  }
});


// *********************************** router for apk

router.get('/android/ip-json/', function (req, res) {
  if (!req.query.startsWith || req.query.startsWith == "") {
    return res.status(400).send({
      success: 'false',
      message: 'startsWith is required'
    });
  } else {
    let startsWith = req.query.startsWith;
    let url = 'http://api.geonames.org/postalCodeSearchJSON?postalcode_startsWith=' + startsWith + '&username=vvcc&country=US&maxRows=5';
    getJsonFromUrl(url, function (obj) {
      let out = []
      obj['postalCodes'].forEach(element => {
        out.push(element['postalCode'])
      });
      res.json(out);
      //console.log("startsWith", startsWith);
    });
  }
});

router.get('/android/google-img/', function (req, res) {
  if (!req.query.productTitle || req.query.productTitle == "") {
    return res.status(400).send({
      success: 'false',
      message: 'productTitle is required'
    });
  } else {
    let productTitle = req.query.productTitle;

    // use your own key
    let googleKEY2 = 'AIzaSXXXXXXXXXXXXXXXXXXXXXXXxx';//caolin
    let googleKEY1 = 'AIzaSXXXXXXXXXXXXXXXXXXXXXXXX';
    let cx2 = '01624954XXXXXXXXXXXXXXXXXXXXX:mqfkqd5-kay';//caolin
    let cx1 = '01739188XXXXXXXXXXXXXXXXXXXXXX:kidf70txplw';
    
    let url;
    if (req.query.v == '1') {
      url = 'https://www.googleapis.com/customsearch/v1?q=' + encodeURI(productTitle) + '&cx=' + cx1 + '&imgSize=huge&imgType=news&num=8&searchType=image&key=' + googleKEY1;

    } else if (req.query.v == '2') {
      url = 'https://www.googleapis.com/customsearch/v1?q=' + encodeURI(productTitle) + '&cx=' + cx2 + '&imgSize=huge&imgType=news&num=8&searchType=image&key=' + googleKEY2;
    }
    //url = 'http://api.myjson.com/bins/6fjxo';
    getJsonFromUrl(url, function (obj) {
      console.log('google url', url);
      let out = []
      if (obj['items']) {
        obj['items'].forEach(element => {
          out.push(element['link'])
        });
      }
      res.json(out);
      console.log("productTitle", productTitle);
    });
  }
});

router.get('/android/similar/', function (req, res) {
  if (!req.query.itemId || req.query.itemId == "") {
    return res.status(400).send({
      success: 'false',
      message: 'itemId is required'
    });
  } else {
    let itemId = req.query.itemId;

    //use your own key
    let url = 'http://svcs.ebay.com/MerchandisingService?OPERATION-NAME=getSimilarItems&SERVICE-NAME=MerchandisingService&SERVICE-VERSION=1.1.0&CONSUMER-ID=LinengCa-vvvvince-PRD-XXXXXXXXXXXXXXXX&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&itemId=' + itemId + '&maxResults=20';
    //let url = 'http://api.myjson.com/bins/1bzlyk';
    getJsonFromUrlGet(url, function (obj) {
      console.log('similar url', url);
      let out = [];
      if(obj['getSimilarItemsResponse']['itemRecommendations']['item']){
        obj['getSimilarItemsResponse']['itemRecommendations']['item'].forEach(element => {
          out.push({
            Image: element['imageURL'],
            Title: element['title'],
            _viewItemURL: element['viewItemURL'],
            Price: (parseFloat(element['buyItNowPrice']['__value__']) == 0 && parseFloat(element['currentPrice']['__value__']) != 0) ? parseFloat(element['currentPrice']['__value__']) : parseFloat(element['buyItNowPrice']['__value__']),
            Shipping: parseFloat(element['shippingCost']['__value__']),
            DaysLeft: parseFloat(timeLeftTrim(element['timeLeft'], 'P', 'D')),
          })
        });
      }
      res.json(out);
      console.log("similar api", itemId);

    });
  }
});

router.get('/android/item-detail/', function (req, res) {
  if (!req.query.itemId || req.query.itemId == "" || req.query.itemId == '-1') {
    return res.status(400).send({
      success: 'false',
      message: 'productTitle is required'
    });
  } else {
    let itemId = req.query.itemId;

    // use your own key
    let url = 'http://open.api.ebay.com/shopping?callname=GetSingleItem&responseencoding=JSON&appid=LinengCa-vvvvince-PRD-160cXXXXXXXXXXXXXXXXXXX&siteid=0&version=967&ItemID=' + itemId + '&IncludeSelector=Description,Details,ItemSpecifics';
    
    //let url = 'http://api.myjson.com/bins/15fltg';
    getJsonFromUrlGet(url, function (obj) {
      console.log('detail url', url);

      if (obj['Ack'] != 'Failure') {
        let out = {}
        out['itemId'] = (obj['Item'] && obj['Item']['ItemID'])?(obj['Item']['ItemID']):null;
        out['title'] = (obj['Item'] && obj['Item']['Title'])?obj['Item']['Title']:null;

        out['viewItemURLForNaturalSearch'] = (obj['Item'] && obj['Item']['ViewItemURLForNaturalSearch'])?(obj['Item']['ViewItemURLForNaturalSearch']):null;

        out['Highlights'] = {};
        (obj['Item'] && obj['Item']['PictureURL']) ? (out['Highlights']['ProductImages'] = obj['Item']['PictureURL']) : null;
        (obj['Item'] && obj['Item']['Subtitle']) ? (out['Highlights']['Subtitle'] = obj['Item']['Subtitle']) : null;
        (obj['Item'] && obj['Item']['CurrentPrice'] && obj['Item']['CurrentPrice']['Value']) ? (out['Highlights']["Price"] = parseFloat(obj['Item']['CurrentPrice']['Value'])) : null;

        if (obj['Item'] && obj['Item']['ItemSpecifics'] && obj['Item']['ItemSpecifics']['NameValueList'].length > 0) {
          
          out['Specifications'] = [];
          obj['Item']['ItemSpecifics']['NameValueList'].forEach(element => {
            if(element['Value'].length>1){
              element['Value'].forEach(va=> {
              
                  out['Specifications'].push(va);
              });
            }else{
              if(element['Value'].toString() != "Yes" && element['Value'].toString() != "No" && element['Name'] != "Brand"){
                out['Specifications'].push(element['Value'].join(''))
              }else if(element['Name'] == "Brand"){
                out['Highlights']['Brand'] = element['Value'].join('');
                out['Specifications'].unshift(element['Value'].join(''));
              }
            }
            
          });
        }
        out['SoldBy'] = {};
        (obj['Item'] && obj['Item']['Storefront'] && obj['Item']['Storefront']['StoreName']) ? (out['SoldBy']['StoreName'] = obj['Item']['Storefront']['StoreName']) : null;
        (obj['Item'] && obj['Item']['Storefront'] && obj['Item']['Storefront']['StoreURL']) ? (out['SoldBy']['StoreURL'] = obj['Item']['Storefront']['StoreURL']) : null;
        (obj['Item'] && obj['Item']['Seller'] && obj['Item']['Seller']['FeedbackScore']) ? (out['SoldBy']['FeedbackScore'] = obj['Item']['Seller']['FeedbackScore']) : null;
        (obj['Item'] && obj['Item']['Seller'] && obj['Item']['Seller']['PositiveFeedbackPercent']) ? (out['SoldBy']['Popularity'] = parseFloat(obj['Item']['Seller']['PositiveFeedbackPercent'])) : null;
        (obj['Item'] && obj['Item']['Seller'] && obj['Item']['Seller']['FeedbackRatingStar']) ? (out['SoldBy']['FeedbackStar'] = obj['Item']['Seller']['FeedbackRatingStar']) : null;

        out['ReturnPolicy'] = {};
        (obj['Item'] && obj['Item']['ReturnPolicy'] && obj['Item']['ReturnPolicy']['ShippingCostPaidBy']) ? (out['ReturnPolicy']['ShippedBy'] = obj['Item']['ReturnPolicy']['ShippingCostPaidBy']) : null;
        (obj['Item'] && obj['Item']['ReturnPolicy'] && obj['Item']['ReturnPolicy']['Refund']) ? (out['ReturnPolicy']['RefundMode'] = obj['Item']['ReturnPolicy']['Refund']) : null;
        (obj['Item'] && obj['Item']['ReturnPolicy'] && obj['Item']['ReturnPolicy']['ReturnsWithin']) ? (out['ReturnPolicy']['ReturnsWithin'] = obj['Item']['ReturnPolicy']['ReturnsWithin']) : null;
        (obj['Item'] && obj['Item']['ReturnPolicy'] && obj['Item']['ReturnPolicy']['ReturnsAccepted']) ? (out['ReturnPolicy']['Policy'] = obj['Item']['ReturnPolicy']['ReturnsAccepted']) : null;

        out['ShippingInfo'] = {};
        (obj['Item']) ? (out['ShippingInfo']['GlobalShipping'] = ((obj['Item']['GlobalShipping'])?"Yes":"No")): null;
        (obj['Item'] && obj['Item']['ConditionDescription']) ? (out['ShippingInfo']['ConditionDescription'] = obj['Item']['ConditionDescription']): null;
        (obj['Item'] && obj['Item']['HandlingTime']) ? (out['ShippingInfo']['HandlingTime'] = ((obj['Item']['HandlingTime'] < 2)?(obj['Item']['HandlingTime']+" Day"):(obj['Item']['HandlingTime']+" Days"))): null;
        
        console.log(out);
        res.json(out);
        console.log("detail api", itemId);
      }
    });
  }
});

router.get('/android/search/', function (req, res) {
  if (!req.query.keyword || req.query.keyword == "") {
    return res.status(400).send({
      success: 'false',
      message: 'keyword is required'
    });
  } else {
    //let keyword = req.query.keyword;
    //let buyerPostalCode = req.query.buyerPostalCode;
    let count = 0;
    let count2 = 0;

    //use your own key
    let url = 'http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=LinengCa-vvvvince-PRD-XXXXXXXXXXXXX&RESPONSE-DATA-FORMAT=JSON&paginationInput.entriesPerPage=50';
      
      if(req.query.keyword){
        url += '&keywords=' + req.query.keyword;
      }
      if(req.query.buyerPostalCode){
        url += '&buyerPostalCode=' + req.query.buyerPostalCode + ((req.query.MaxDistance) ? ('&itemFilter(' + count++ + ').name=MaxDistance&itemFilter(' + (count - 1) + ').value=' + req.query.MaxDistance) : '');
      }
      
      if(req.query.FreeShippingOnly){
        url += '&itemFilter(' + count++ + ').name=FreeShippingOnly&itemFilter(' + (count - 1) + ').value=' + req.query.FreeShippingOnly;
      }

      if(req.query.LocalPickupOnly){
        url+='&itemFilter(' + count++ + ').name=LocalPickupOnly&itemFilter(' + (count - 1) + ').value=' + req.query.LocalPickupOnly;
      }

      if(true){
        url += '&itemFilter(' + count++ + ').name=HideDuplicateItems&itemFilter(' + (count - 1) + ').value=true';
      }
      if(req.query.Condition){
        url+='&itemFilter(' + count++ + ').name=Condition'
        if(req.query.New){
          url+='&itemFilter(' + (count - 1) + ').value(' + count2++ + ')=New';
        }
        if(req.query.Used){
          url+='&itemFilter(' + (count - 1) + ').value(' + count2++ + ')=Used';
        }
        if(req.query.Unspecified){
          url+='&itemFilter(' + (count - 1) + ').value(' + count2++ + ')=Unspecified';
        }
      }

      if(req.query.categoryId){
        url+='&categoryId=' + (req.query.categoryId);
      }

      url+='&outputSelector(0)=SellerInfo&outputSelector(1)=StoreInfo';

    console.log(url);

    getJsonFromUrlGet(url, function (obj) {
      
      try {
        let out = [];

        if (obj['findItemsAdvancedResponse'] && obj['findItemsAdvancedResponse'][0]['searchResult'] && obj['findItemsAdvancedResponse'][0]['searchResult'][0]['item'].length>0){
          obj['findItemsAdvancedResponse'][0]['searchResult'][0]['item'].forEach(element => {
            let el = {
              image: element['galleryURL'] ? element['galleryURL'][0] : 'N/A',
              title: element['title'] ? element['title'][0] : 'N/A',
              price: (element['sellingStatus'] && element['sellingStatus'][0]['currentPrice'] && element['sellingStatus'][0]['currentPrice'][0]['__value__']) ? parseFloat(element['sellingStatus'][0]['currentPrice'][0]['__value__']).toFixed(2) : 'N/A',
              shipping: (element['shippingInfo'] && element['shippingInfo'][0]['shippingServiceCost']) ? parseFloat(element['shippingInfo'][0]['shippingServiceCost'][0]['__value__']).toFixed(2) : 'N/A',
              zip: (element['postalCode']) ? (element['postalCode'][0]) : 'N/A',
              condition: (element['condition'] && element['condition'][0]['conditionDisplayName']) ? (element['condition'][0]['conditionDisplayName'][0]) : 'N/A',
              isWish: false,
              itemId: (element['itemId']) ? (element['itemId'][0]) : null
            }
            
            el['ShippingInfo'] = {}  //for shipping
            // if(element['shippingInfo'] && element['shippingInfo'][0]['shippingServiceCost'] && element['shippingInfo'][0]['shippingServiceCost'][0]['__value__']){
            //   el['ShippingInfo']['shippingServiceCost'] = element['shippingInfo'][0]['shippingServiceCost'][0]['__value__']
            // }
            if(element['shippingInfo'] && element['shippingInfo'][0]['shipToLocations']){
              el['ShippingInfo']['GobalShipping'] = (element['shippingInfo'][0]['shipToLocations'][0] == 'Worldwide') + "";
            }
            if(element['shippingInfo'] && element['shippingInfo'][0]['handlingTime']){
              el['ShippingInfo']['handlingTime'] = (parseFloat(element['shippingInfo'][0]['handlingTime'][0])<=1)? (element['shippingInfo'][0]['handlingTime'][0].toString() + ' Day'):(element['shippingInfo'][0]['handlingTime'][0].toString() + ' Days');
            }
            // if(element['condition'] && element['condition'][0]['conditionDisplayName']){
            //   el['ShippingInfo']['condition'] = element['condition'][0]['conditionDisplayName'][0]
            // }
  
            el['SoldBy'] = {}  //for Seller
            if (element['storeInfo'] && element['storeInfo'][0]['storeName']){
              el['SoldBy']['storeName'] = element['storeInfo'][0]['storeName'][0]
            }
            if (element['storeInfo'] && element['storeInfo'][0]['storeURL']){
              el['SoldBy']['storeURL'] = element['storeInfo'][0]['storeURL'][0]
            }
            if (element['sellerInfo'] && element['sellerInfo'][0]['feedbackScore']){
              el['SoldBy']['feedbackScore'] = parseFloat(element['sellerInfo'][0]['feedbackScore'][0])
            }
            if (element['sellerInfo'] && element['sellerInfo'][0]['positiveFeedbackPercent']) {
              el['SoldBy']['positiveFeedbackPercent'] = Math.round(parseFloat(element['sellerInfo'][0]['positiveFeedbackPercent'][0]))
            }
            if (element['sellerInfo'] && element['sellerInfo'][0]['feedbackRatingStar']) {
              el['SoldBy']['feedbackRatingStar'] = element['sellerInfo'][0]['feedbackRatingStar'][0]
            }
            console.log(el);
            out.push(el);
  
          })
        }
        res.json(out);
        console.log("search api", url);
      }catch(e){
        console.log(e)
        res.json([]);
      }
    });
  }
});

//

const server = http.createServer(app);

function timeLeftTrim(str, a, b) {
  let n = str.substring(str.indexOf(a) + 1, str.indexOf(b));
  return n;
}

function getJsonFromUrl(url, callback) {
  request(url, {
    json: true
  }, (err, res, body) => {
    if (err) {} else {
      callback(body);
    }
  });
}

function getJsonFromUrlGet(url, callback) {
  http.get(url, function (res) {
    let body = '';
    res.on('data', function (chunk) {
      body += chunk;
    });
    res.on('end', function () {
      let data = JSON.parse(body);
      callback(data);
    });
  }).on('error', function (e) {
    console.log("Got an error: ", e);
  });
}

server.listen((process.env.PORT || 8080), () => console.log(`running port: ${process.env.PORT || 8080}`));
