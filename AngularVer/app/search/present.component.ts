import { Component, OnInit, Input } from '@angular/core';
import { trigger, transition, animate, style, state, query, group } from '@angular/animations';
import { DataService } from '../data.service';



@Component({
  selector: 'app-present',
  templateUrl: './present.component.html',
  styleUrls: ['./present.component.css'],
  animations: [
    trigger('anime', [
      transition('* <=> *', [
        //need change
        query(':enter, :leave', 
        style({ position: 'absolute', width: '100%' }),
        { optional: true }),
        group([
          query(':enter', [
            style({ transform: 'translateX(-100%)', opacity: '0.4' }),
            animate('400ms 100ms ease', style({ transform: 'translateX(0%)', opacity: '1' }))
          ],
          { optional: true }),
          query(':leave', [
            style({ transform: 'translateX(0%)', opacity: '1' }),
            animate('300ms ease', style({ transform: 'translateX(100%)', opacity: '0.4' }))
          ],
          { optional: true }),
        ])
      ])
    ])
  ]
})
export class PresentComponent implements OnInit {
  // @Input()
  // formInfoJson: any;
  constructor(private data: DataService) { }
  isLoad = false; //no progress
  currentTabIndex = 0;

  tabs = [{
    index: 0,
    name: "Results",
    isActive: false
  }, {
    index: 1,
    name: "Wish List",
    isActive: false
  }]

  ngOnInit() {
    this.toogleTab(0);
  }

  toogleTab(tabIndex) {
    this.tabs[this.currentTabIndex].isActive = false;
    this.currentTabIndex = tabIndex;
    this.data.listTabIndex = tabIndex;
    this.tabs[this.currentTabIndex].isActive = true;
  }

  returnRouterState(outlet) {
    return outlet.activatedRouteData.state;
  }

}
