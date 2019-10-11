<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Product Search</title>
    <link rel="stylesheet" href="main.css">
    <!-- <style>
        
    </style> -->
</head>

<body>
    <div class="product-search-div">
        <div class="search-box-div">
            <div class="search-div">
                <div class="search-title-div">
                    <a id="title">Product Search</a>
                    <hr>
                </div>
                <div>
                    <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="post" id="searchForm" name="searchForm"></form>

                    <div class="search-fli-line" id="keyword-div">
                        <a class="bold">Keyword</a>
                        <input id="keywords-input" type="text" name="keywords" form="searchForm" required value="<?php echo isset($_POST["keywords"]) ? $_POST["keywords"] : "" ?>">

                    </div>
                    <div class="search-fli-line" id="category-div">
                        <a class="bold">Category</a>
                        <select id="cate-select" name="cate" form="searchForm">
                            <option id="all-opt" value="-1">All Categories</option>
                            <option value="null" disabled>----------------------------------------------</option>
                            <option value="550">Art</option>
                            <option value="2984">Baby</option>
                            <option value="267">Books</option>
                            <option value="11450">Clothing, Shoes & Accessories</option>
                            <option value="58058">Computers/Tablets & Networking</option>
                            <option value="26395">Health & Beauty</option>
                            <option value="11233">Music</option>
                            <option value="1249">Video Games & Consoles</option>
                        </select>
                        <script>
                            <?php if (!isset($_POST["cate"])){ ?>
                                document.getElementById('cate-select').value = "-1";
                            <?php }else{?>
                                document.getElementById('cate-select').value = "<?php echo $_POST["cate"];?>";
                            <?php } ?>
                        </script>
                    </div>
                    <div class="search-fli-line" id="condition-div">
                        <a class="bold">Condition</a>
                        <input class="checkbox" name="c_new" type="checkbox" form="searchForm" id="c_new" <?php echo isset($_POST["c_new"]) ? "checked='checked'" : "" ?>><a>New</a>
                        <input class="checkbox" name="c_used" type="checkbox" form="searchForm" id="c_used" <?php echo isset($_POST["c_used"]) ? "checked='checked'" : "" ?>><a>Used</a>
                        <input class="checkbox" name="c_unsp" type="checkbox" form="searchForm" id="c_unsp" <?php echo isset($_POST["c_unsp"]) ? "checked='checked'" : "" ?>><a>Unspecified</a>
                    </div>
                    <div class="search-fli-line" id="shipping-div">
                        <a class="bold">Shipping Options</a>
                        <input class="checkbox" name="localpickuponly" type="checkbox" form="searchForm" id="localpickuponly" <?php echo isset($_POST["localpickuponly"]) ? "checked='checked'" : "" ?>><a>Local Pickup</a>
                        <input class="checkbox" name="freeshippingonly" type="checkbox" form="searchForm" id="freeshippingonly" <?php echo isset($_POST["freeshippingonly"]) ? "checked='checked'" : "" ?>><a>Free Shipping</a>
                    </div>
                    <div class="search-fli-line inline" id="nearby-div">
                        <div>
                            <input class="checkbox" type="checkbox" name="isNearby" id="nearBySwitch" onchange="switchOne(this);" form="searchForm" <?php echo isset($_POST["isNearby"]) ? "checked='checked'" : "" ?>>
                            <a class="bold">Enable Nearby Search</a>
                        </div>
                    
                    <div class="mile-distance-div" id="nearByDiv">
                            <div><input id="mile-distance" type="text" name="maxdistance" form="searchForm" placeholder="10" value="<?php echo isset($_POST["maxdistance"]) ? $_POST["maxdistance"] : "" ?>"><a>miles from</a></div>

                            <div>
                                <div>
                                    <input type="radio" name="location" id="radio-local" value="local" form="searchForm" onchange="disablePanelTwo();" 
                                        <?php 
                                            if ((isset($_POST["location"]) && $_POST["location"]=="local") || !isset($_POST["location"])):
                                                echo "checked";
                                            endif;
                                        ?>                                
                                    >
                                    <a>Here</a>
                                </div>
                                <div>
                                    <input type="radio" name="location" id="radio-custom" value="custom" form="searchForm" onchange="enablePanelTwo();" <?php if (isset($_POST["location"]) && $_POST["location"]=="custom") echo "checked";?>>
                                    <input type="text" name='buyerPostalCode' placeholder="zip code" id="postal-custom" form="searchForm" required value="<?php echo isset($_POST['buyerPostalCode']) ? $_POST['buyerPostalCode'] : "" ?>">
                                </div>

                                <script>
                                <?php if (isset($_POST["location"]) && $_POST["location"]=="local"):?>
                                    document.getElementById('postal-custom').disabled = true;
                                <?php elseif(isset($_POST["location"]) && $_POST["location"]=="custom"):?>
                                    if(document.getElementById('nearBySwitch').checked){
                                        document.getElementById('postal-custom').disabled = false;
                                    }
                                <?php endif;?>
                                </script>

                            </div>
                        </div>      
                        
                        <script>
                            <?php if(!isset($_POST["isNearby"])):?>
                                document.getElementById('nearByDiv').style.opacity = 0.5;
                                var inputList = ["mile-distance", "radio-local", "radio-custom", "postal-custom"];
                                inputList.forEach(element => {
                                    document.getElementById(element).disabled = true;
                                });
                                
                                document.getElementById('nearBySwitch').checked = false;
                            <?php else:?>
                                document.getElementById('nearBySwitch').checked = <?php echo isset($_POST["isNearby"]) ? true : false?>;
                            <?php endif;?>
                        </script>

                    </div>
                    <input type="hidden" id="clientPostalCode" name="clientPostalCode" form="searchForm" value="<?php echo isset($_POST['clientPostalCode']) ? $_POST['clientPostalCode'] : "" ?>">
                    <input type="hidden" id="itemId" name="itemId" form="searchForm" value="<?php echo isset($_POST['itemId']) ? $_POST['itemId'] : "-1" ?>">
                    <div class="search-fli-line submit-div">
                        <input id="submit-btn" type="submit" name="submit-btn" value="Search" form="searchForm" <?php echo isset($_POST["clientPostalCode"]) ? "" : "disabled" ?> onclick="clearItemId();")>
                        <input type="button" name="clear" value="Clear" onclick="reset();">
                    </div>

                </div>
                
            </div>
        </div>
       
        <div class="search-box-div" id="search-tb-div"></div>
        <div class="search-box-div" id="detail-tb-div"></div>
        <div class="debug-div"></div>
    </div>


    <?php if(!isset($_POST['clientPostalCode'])):?>
        <script>
            var geoJsonObject = getXhr("http://ip-api.com/json/");
            var ip_zip = geoJsonObject.zip;
            console.log("ip-api.com:", ip_zip);
            let clientPostalCode = document.getElementById('clientPostalCode');
            if (/^[0-9]{5}$/.test(ip_zip)){
                console.log(ip_zip + " match");
                clientPostalCode.value = ip_zip;
            }else{
                console.error(ip_zip + " zip error!");
                clientPostalCode.value = "-1";
            }

            function getXhr(urlPath) {
                var xmlHttp;
                if (window.XMLHttpRequest) { // code for IE7+, Firefox, Chrome, Opera, Safari
                    xmlHttp = new XMLHttpRequest();
                } else { // code for IE6, IE5
                    xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
                }
                xmlHttp.open("GET", urlPath, false); // synchronous request
                xmlHttp.onerror = function (e) {
                    console.log(xmlHttp.statusText);
                    return null;
                };
                xmlHttp.send(null);
                var xmlResponse = xmlHttp.responseText
                if (xmlResponse) {
                    try {
                        return JSON.parse(xmlResponse);
                    } catch (e) {
                        console.error(e);
                        return null;
                    }
                }
            }
            document.getElementById('submit-btn').disabled = false;
            console.log("json parse success!!");
        </script>
    <?php else:?>
        <?php 
            if(isset($_POST['itemId']) && $_POST['itemId'] == "-1"):
                if (!isset($_POST['buyerPostalCode']) && isset($_POST["location"])) {                   
                    $_POST['buyerPostalCode'] =  $_POST['clientPostalCode'];
                }

                if (!preg_match("/^[0-9]{5}$/", $_POST['buyerPostalCode']) && isset($_POST["location"])){
                    $_POST['buyerPostalCode'] = "-1";
                }
                
                if ($_POST['buyerPostalCode'] == "-1"): ?> 
                    <script>
                        //alert zipcode invalid
                        let tb_div = document.getElementById('search-tb-div');
                        tb_div.innerHTML = "";
                        let alert_div = document.createElement('div');
                        alert_div.innerHTML = "<a>Zipcode is invalid</a>";
                        alert_div.id = "alert-div";
                        tb_div.appendChild(alert_div);
                    </script>
                <?php else: ?> 
                    
                    <?php
                        $APPID = "LinengCa-vvvvince-PRD-160c321e5-3540d155"; //your App-ID
                        $url = "http://svcs.ebay.com/services/search/FindingService/v1";
                        $url .= "?OPERATION-NAME=findItemsAdvanced";
                        $url .= "&SERVICE-VERSION=1.0.0";
                        $url .= "&SECURITY-APPNAME=" . $APPID;
                        $url .= "&RESPONSE-DATA-FORMAT=JSON";
                        $url .= "&REST-PAYLOAD";
                        $url .= "&paginationInput.entriesPerPage=20";
                        $url .= "&keywords=" . urlencode($_POST["keywords"]);
                        
                        $itemFilter = array();

                        if (isset($_POST["cate"]) && $_POST["cate"] != "-1"):
                            $url .= "&categoryId=" . $_POST["cate"];
                        endif;                   

                        //postcode
                        if(isset($_POST["isNearby"])){
                            $url .= "&buyerPostalCode=" . $_POST['buyerPostalCode'];
                            if ($_POST["maxdistance"]=="") {$_POST["maxdistance"] = "10";}
                            $f = [
                                "name" => "MaxDistance",
                                "value" => (string)$_POST["maxdistance"]
                            ];
                            array_push($itemFilter, $f);
                        }
                        
                        //HideDuplicateItems
                        $f = [
                            "name" => "HideDuplicateItems",
                            "value" => "true"
                        ];

                        array_push($itemFilter, $f);

                        //localpickuponly
                        if (isset($_POST["localpickuponly"])) {
                            $f = [
                                "name" => "LocalPickupOnly",
                                "value" => "true"
                            ];
                            array_push($itemFilter, $f);
                        }

                        //freeshippingonly
                        if (isset($_POST["freeshippingonly"])) {
                            $f = [
                                "name" => "FreeShippingOnly",
                                "value" => "true"
                            ];
                            array_push($itemFilter, $f);
                        }

                        //condition
                        if (isset($_POST["c_new"]) || isset($_POST["c_used"]) || isset($_POST["c_unsp"])){
                            
                            $fv = array();

                            if (isset($_POST["c_new"])) {
                                array_push($fv, "New");
                            }
                            if (isset($_POST["c_used"])) {
                                array_push($fv, "Used");
                            }
                            if (isset($_POST["c_unsp"])) {
                                array_push($fv, "Unspecified");
                            }
                            $f = [
                                "name" => "Condition",
                                "value" => $fv
                            ];

                            array_push($itemFilter, $f);
                        }


                        for ($i = 0; $i < count($itemFilter); $i++){
                            if($itemFilter[$i]["name"] == "Condition"){
                                $url .= "&itemFilter(" . $i . ").name=" . $itemFilter[$i]["name"];
                                for($j = 0; $j < count($itemFilter[$i]["value"]); $j++){
                                    $url .= "&itemFilter(" . $i . ").value(".$j.")=" . $itemFilter[$i]["value"][$j];
                                }
                            }else{
                                $url .= "&itemFilter(" . $i . ").name=" . $itemFilter[$i]["name"];
                                $url .= "&itemFilter(" . $i . ").value=" . $itemFilter[$i]["value"];
                            }
                            
                        }

                        $json = json_decode(file_get_contents($url), true);

                        $items = $json['findItemsAdvancedResponse'][0]['searchResult'][0]['item'];

                    ?>

                    <?php if(!$items):?>
                        <script>
                            //alert no record
                            let tb_div = document.getElementById('search-tb-div');
                            tb_div.innerHTML = "";
                            let alert_div = document.createElement('div');
                            
                        <?php if($json['findItemsAdvancedResponse'][0]['ack'][0] == "Success"){?>
                            alert_div.innerHTML = "<a>No Records has been found</a>";
                        <?php }else{ ?>
                            alert_div.innerHTML = "<a><?php echo $json['findItemsAdvancedResponse'][0]['errorMessage'][0]['error'][0]['message'][0]?></a>";
                        <?php } ?>
                        
                            alert_div.id = "alert-div";
                            tb_div.appendChild(alert_div);
                        </script>
                        
                    <?php else:
                        $index = 1;
                        $lst = array();
                        foreach ($items as $item) {
                            
                            $photo_url = $item['galleryURL'][0];

                            $_title = $item['title'][0];
                            $_itemId = $item['itemId'][0];

                            $price = '$'.$item['sellingStatus'][0]['currentPrice'][0]['__value__'];
                            $zipcode = $item['postalCode'][0];
                            if (!$zipcode){
                                $zipcode = "N/A";
                            }
                            $condition = $item['condition'][0]['conditionDisplayName'][0];
                            if (!$condition){
                                $condition = "N/A";
                            }
                            $shipping = $item['shippingInfo'][0]['shippingServiceCost'][0]['__value__'];
                            
                            if (!$shipping){
                                $shipping = "N/A";
                            }else{
                                if ($shipping == "0.00"): $shipping = "Free Shipping";
                                else: $shipping = '$'.$shipping;
                                endif;
                            }
                            
                            $it = [
                                "Index" => $index,
                                "Photo" => $photo_url,
                                "Name" => [
                                    "title" => $_title,
                                    "_itemId" => $_itemId    
                                ],
                                "Price" => $price,
                                "Zip code" => $zipcode,
                                "Condition" => $condition,
                                "Shipping Option" => $shipping
                            ];
                            array_push($lst, $it);
                            $index += 1;
                        }
                
                        $myJSON = json_encode($lst);

                        // echo "<div>".$myJSON."</div>";

                        // $html .= "</table>";
                        // echo $html;
                        ?>
                        <script>
                            //generate the table
                            function createTable(objs){
                                var table = document.createElement('table');
                                table.setAttribute("id", "search-tb");
                                var headerList = Object.keys(objs[0]);
                                let row = table.insertRow();
                                for(let index in headerList){
                                    //make table head
                                    let head = document.createElement("th");
                                    head.appendChild(document.createTextNode(headerList[index]));
                                    row.appendChild(head);
                                }
                                for (let i = 0; i < objs.length; i++) {
                                    let row = table.insertRow();
                                    for (let k in objs[i]){ 
                                        let cell = row.insertCell();
                                        if(k=="Photo"){
                                            //create photo in table
                                            let img = document.createElement('img');
                                            img.setAttribute("src", objs[i][k]);
                                            cell.appendChild(img);
                                            cell.setAttribute("id", "img-td");
                                        }else if(k=="Name"){
                                            //make link archor
                                            let a = document.createElement('a');                                  
                                            a.setAttribute("onclick", "openDescription(\""+ objs[i][k]._itemId +"\");return false;");
                                            a.setAttribute("href", "#");
                                            a.appendChild(document.createTextNode(objs[i][k].title));
                                            a.id="search-title";
                                            cell.appendChild(a);
                                        }else{
                                            //apppend price and location
                                            cell.appendChild(document.createTextNode(objs[i][k]));
                                        }
                                    }
                                }

                                document.getElementById('search-tb-div').appendChild(table);
                            }

                            var jsonSearch = <?php echo $myJSON;?>;
                            createTable(jsonSearch);
                        </script>
                    <?php endif;?>
                <?php endif;?>
            <?php else:
                $APPID = "LinengCa-vvvvince-PRD-160c321e5-3540d155"; //your AppID

                //get detail
                $src = "http://open.api.ebay.com/shopping?callname=GetSingleItem&responseencoding=JSON&appid=".$APPID."&siteid=0&version=967&ItemID=".$_POST['itemId']."&IncludeSelector=Description,Details,ItemSpecifics";
                $jsonSrc = json_decode(file_get_contents($src), true);

                $_PictureURL = $jsonSrc['Item']['PictureURL'];
                $_Title = $jsonSrc['Item']['Title'];
                $_Subtitle = $jsonSrc['Item']['Subtitle'];
                $_Price = $jsonSrc['Item']['CurrentPrice']['Value']." ".$jsonSrc['Item']['CurrentPrice']['CurrencyID'];
                $_Location = $jsonSrc['Item']['PostalCode']?($jsonSrc['Item']['Location'].", ".$jsonSrc['Item']['PostalCode']):$jsonSrc['Item']['Location'];
                $_Seller = $jsonSrc['Item']['Seller']['UserID'];
                
                if($jsonSrc['Item']['ReturnPolicy']){
                    if($jsonSrc['Item']['ReturnPolicy']['ReturnsWithin']){
                        $_ReturnPolicy = $jsonSrc['Item']['ReturnPolicy']['ReturnsAccepted'] . " Within " . $jsonSrc['Item']['ReturnPolicy']['ReturnsWithin'];
                    }else{
                        $_ReturnPolicy = $jsonSrc['Item']['ReturnPolicy']['ReturnsAccepted'];
                    }
                }else{
                    $_ReturnPolicy = "-1";
                }
                $_itemSpec = $jsonSrc['Item']['ItemSpecifics']['NameValueList'];

                $_Description = $jsonSrc['Item']['Description'];

                $dlist = [
                    "Photo" => $_PictureURL?$_PictureURL:"-1",
                    "Title" => $_Title?$_Title:"-1",
                    "Subtitle" => $_Subtitle?$_Subtitle:"-1",
                    "Price" => $jsonSrc['Item']['CurrentPrice']['Value']?$_Price:"-1",
                    "Location" => $jsonSrc['Item']['Location']?$_Location:"-1",
                    "Seller" => $_Seller?$_Seller:"-1",
                    "Return Policy(US)" => $_ReturnPolicy,
                    "_itemSpec" => $_itemSpec?$_itemSpec:"-1",
                    "_Description" => $_Description?$_Description:"-1"
                ];

                //get similar

                $rlist = array();
                $related = "http://svcs.ebay.com/MerchandisingService?OPERATION-NAME=getSimilarItems&SERVICE-NAME=MerchandisingService&SERVICE-VERSION=1.1.0&CONSUMER-ID=".$APPID."&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&itemId=".$_POST['itemId']."&maxResults=8";
                $jsonRelated = json_decode(file_get_contents($related), true);
                $rItem = $jsonRelated['getSimilarItemsResponse']['itemRecommendations']['item'];

                for ($i=0; $i < count($rItem); $i++) { 
                    $r_itemId = $rItem[$i]['itemId'];
                    $r_title = $rItem[$i]['title'];
                    $r_imageURL = $rItem[$i]['imageURL'];
                    $r_buyItNowPrice = $rItem[$i]['currentPrice']['__value__']?$rItem[$i]['currentPrice']['__value__']:$rItem[$i]['buyItNowPrice']['__value__'];
                    
                    $rl = [
                        "r_imageURL" => $r_imageURL?$r_imageURL:"N/A",
                        "r_title" => $r_title?$r_title:"N/A",
                        "r_buyItNowPrice" => $r_buyItNowPrice?'$'.$r_buyItNowPrice:"N/A",
                        "r_itemId" => $r_itemId
                    ];

                    array_push($rlist, $rl);
                }

                $mydJSON = json_encode($dlist);
                $myrJSON = json_encode($rlist);?>
                
                <script>
                    console.log("Detail Load");
                    //createDetail Page
                    var jsonDetail = <?php echo $mydJSON;?>;
                    var jsonSimilar = <?php echo $myrJSON;?>;
                    createDetailTable(jsonDetail);
                    createMessagePage(jsonDetail);
                    createSimilarPage(jsonSimilar);

                    function createDetailTable(objs){
                        let h1 = document.createElement('h1');
                        h1.innerHTML = "Item Details";
                        h1.id = "detail-h1";
                        let d_table = document.createElement('table');
                        d_table.id = "detail-tb";
                        for (let k in objs){
                            let v = objs[k];
                            if(k.indexOf('_') == -1 && v !="-1"){
                                let tr = document.createElement('tr');
                                if(k == "Photo"){
                                    tr.innerHTML = "<td>"+k+"</td><td><img src=\""+v+"\"></td>";
                                }else{
                                    tr.innerHTML = "<td>"+k+"</td><td>"+v+"</td>";
                                }
                                d_table.appendChild(tr);
                            }
                            
                            if(k == "_itemSpec"){
                                if(v == "-1"){
                                    console.log("No Detail Spec");
                                    // let tr = document.createElement('tr');
                                    // tr.innerHTML = "<td>No Detail Info from Seller</td><td style=\"background:#D9D8DA;\"></td>";
                                    // d_table.appendChild(tr);
                                }else{
                                    v.forEach(rw => {
                                        let tr = document.createElement('tr');
                                        tr.innerHTML = "<td>"+rw["Name"]+"</td><td>"+rw["Value"][0]+"</td>";
                                        d_table.appendChild(tr);
                                    });
                                }
                            }
                        }

                        if(!d_table.hasChildNodes()){
                            console.log("No detail found in the itemID, which might be not a singleItem");
                            let tr = document.createElement('tr');
                            tr.innerHTML = "<td id=\"detail-error-td\">No detail info from this multi-item page.</td>";
                            d_table.appendChild(tr);
                        }

                        document.getElementById('detail-tb-div').appendChild(h1);
                        document.getElementById('detail-tb-div').appendChild(d_table);
                    }

                    function createMessagePage(objs){
                        let div = document.createElement('div');
                        div.className = 'detail-div-sm';
                        div.id = 'message-div';
                        let a = document.createElement('a');
                        a.id = 'message-a';
                        a.innerHTML = "click to show seller message";
                        let img = document.createElement('img');
                        img.className = 'arrow';
                        img.id='arrow-message';
                        img.src = "http://csci571.com/hw/hw6/images/arrow_down.png";
                        img.setAttribute("onclick", "toggleMessage(this);");
                        img.style="transform: rotate(0deg);";

                        let iframe;
                        if(objs['_Description'] && objs['_Description'] != "-1"){
                            iframe = document.createElement('iframe');
                            iframe.id='frame-page';
                            iframe.setAttribute("scrolling", "no");
                            iframe.srcdoc = objs['_Description']; // TODO: //if success
                        }else{
                            console.log("No message");
                            iframe = document.createElement('div');
                            iframe.id='frame-page';
                            iframe.className = 'message-error-div';
                            iframe.innerHTML = "<a>No Seller Message Found.</a>"
                        }
                        div.appendChild(a);
                        div.appendChild(img);
                        div.appendChild(iframe);
                        document.getElementById('detail-tb-div').appendChild(div);
                    }

                    function createSimilarPage(objs){
                        let div = document.createElement('div');
                        div.className = 'detail-div-sm';
                        div.id = 'similar-div';
                        let a = document.createElement('a');
                        a.id = 'similar-a';
                        a.innerHTML = "click to show similar items";
                        let img = document.createElement('img');
                        img.className = 'arrow';
                        img.id='arrow-similar';
                        img.src = "http://csci571.com/hw/hw6/images/arrow_down.png";
                        img.setAttribute("onclick", "toggleSimilar(this);");
                        img.style="transform: rotate(0deg);";

                        let sim_div;
                        if(objs.length > 0){
                            console.log("similar", objs.length);
                            sim_div = document.createElement('div');
                            sim_div.id = "similar-item-div";
                            objs.forEach(obj => {
                                let grid_div = document.createElement('div');
                                grid_div.className = "similar-item-sm";
                                let flex_div = document.createElement('div');
                                flex_div.id = "flex-div";
                                let similar_img = document.createElement('img');
                                similar_img.className = "similar-item-img";
                                similar_img.src = obj['r_imageURL'];
                                let similar_a = document.createElement('a');
                                similar_a.className = "similar-title";
                                similar_a.href = "#";
                                similar_a.setAttribute("onclick", "openDescription(\""+ obj['r_itemId'] +"\");return false;");
                                similar_a.innerHTML = obj['r_title'];
                                let similar_p = document.createElement('p');
                                similar_p.className = "similar-price";
                                similar_p.innerHTML = obj['r_buyItNowPrice'];

                                grid_div.appendChild(flex_div);
                                grid_div.appendChild(similar_img);
                                grid_div.appendChild(similar_a);
                                grid_div.appendChild(similar_p);
                                sim_div.appendChild(grid_div);
                            });
                        }else{
                            sim_div = document.createElement('div');
                            sim_div.id = "similar-item-div";
                            sim_err = document.createElement('div');
                            sim_err.className = "similar-error-div";
                            sim_err.innerHTML = "<a>No Similar Item Found.</a>";
                            sim_div.appendChild(sim_err);
                        }
                        
                        div.appendChild(a);
                        div.appendChild(img);
                        div.appendChild(sim_div);
                        document.getElementById('detail-tb-div').appendChild(div);

                    }
                </script>
                
            <?php
            endif;

            //console div
            echo "<div class=\"debug-div\">";
            echo "<div><pre>";
            foreach($_POST as $key => $value) {
                if ($key !== 'submit-btn') {
                    echo $key . " = " . $value . "\n";
                }
            }
            echo "</pre>";
            echo "<div><pre>";
            foreach ($dlist as $key => $value) {
                if ($key !== '_Description') {
                    echo $key . " = " . $value . "\n";
                }
            }
            echo "</pre><a href=\"{$src}\">detailsrc</a></div>";
            echo "<div class=\"debug-div\" style=\"left: 300px;width:70px;\"><a href=\"{$related}\">related</a></div>";
            echo "<div class=\"debug-div\" style=\"left: 300px;top: 50px;width:70px;\"><a href=\"".$url."\">RawJson</a></div>";

    endif;?>
</body>
<script>
    function switchOne(checkbox) {
        if (checkbox.checked == true) {
            console.log("near checked");
            enablePanelOne();
        } else {
            console.log("near unchecked");
            disablePanelOne();
        }
    }

    function disablePanelOne() {
        document.getElementById('nearByDiv').style.opacity = 0.5;
        var inputList = ["mile-distance", "radio-local", "radio-custom", "postal-custom"];
        inputList.forEach(element => {
            document.getElementById(element).disabled = true;
        });
    }

    function disablePanelTwo() {
        document.getElementById('postal-custom').disabled = true;
    }

    function enablePanelOne(){
        document.getElementById('nearByDiv').style.opacity = 1;
        var inputList = ["mile-distance", "radio-local", "radio-custom"];
        inputList.forEach(element => {
            document.getElementById(element).disabled = false;
        });
        if (isSelectMan()){
            document.getElementById('postal-custom').disabled = false;
        }
    }

    function enablePanelTwo() {
        document.getElementById('postal-custom').disabled = false;
    }

    function isSelectMan(){
        return document.getElementById('radio-custom').checked;
    }

    function reset() {
        var inputText = ["keywords-input", "postal-custom"];
        inputText.forEach(element => {
            document.getElementById(element).value = "";
        });

        var uncheckList = ["c_new", "c_used", "c_unsp", "localpickuponly", "freeshippingonly", "radio-custom", "nearBySwitch"];
        uncheckList.forEach(element => {
            document.getElementById(element).checked = false;
        });

        document.getElementById('cate-select').value = "-1";
        document.getElementById('mile-distance').value = "";

        document.getElementById('nearByDiv').style.opacity = 0.5;
        document.getElementById('radio-local').checked = true;

        var inputList = ["mile-distance", "radio-local", "radio-custom", "postal-custom"];
        inputList.forEach(element => {
            document.getElementById(element).disabled = true;
        });

        document.getElementById('search-tb-div').innerHTML ="";
        document.getElementById('detail-tb-div').innerHTML ="";
    }

    //clear item input value each time using the search btn
    function clearItemId(){
        document.getElementById('itemId').value = "-1";
    }

    function toggleMessage(image){
        if(image.style.transform == "rotate(0deg)"){
            //turn on
            turnOnMessage(image);
            turnOffSimilar(document.getElementById('arrow-similar'));
        }else{
            //turn off
            turnOffMessage(image);
        }
    }

    function turnOnMessage(image){
        image.style.transform = "rotate(180deg)";
        let frame = document.getElementById('frame-page');
        frame.style.display = "block";
        try{
            frame.style.height = "30px";
            frame.style.height = (frame.contentWindow.document.body.scrollHeight + 30) + 'px';
        }catch(e){
        }
        document.getElementById('message-a').innerHTML = "click to hide seller message";            

    }

    function turnOffMessage(image){
        image.style.transform = "rotate(0deg)";
        document.getElementById('frame-page').style.display = "none";
        document.getElementById('message-a').innerHTML = "click to show seller message";
    }

    function toggleSimilar(image){
        if(image.style.transform == "rotate(0deg)"){
            //turn on
            turnOnSimilar(image);
            turnOffMessage(document.getElementById('arrow-message'));
        }else{
            //turn off
            turnOffSimilar(image);
        }
    }

    function turnOnSimilar(image){
        image.style.transform = "rotate(180deg)";
        document.getElementById('similar-item-div').style.display = "flex";
        document.getElementById('similar-a').innerHTML = "click to hide similar items";

    }

    function turnOffSimilar(image){
        image.style.transform = "rotate(0deg)";
        document.getElementById('similar-item-div').style.display = "none";
        document.getElementById('similar-a').innerHTML = "click to show similar items";

    }

    function openDescription(itemId){
        document.getElementById('itemId').value = itemId;
        console.log("itemID:", itemId);
        document.getElementById('searchForm').submit();
    }

</script>
</html>
