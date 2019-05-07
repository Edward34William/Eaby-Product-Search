import { Component, OnInit } from '@angular/core';
import { DataService } from 'src/app/data.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html'
})

export class ListComponent implements OnInit {

  constructor(public data: DataService) { }
  ngOnInit() {
    //console.log(this.data.listTabIndex)
  }

}
