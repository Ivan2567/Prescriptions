import { Component, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { SmallLec } from '../classes/SmallLec';
import { SomeDataService } from '../services/some-data.service';
import { LecDataService } from '../services/lec-data.sevice';
import { TokenDataService } from '../services/token-data.service';

import axios from 'axios';

@Component({
  selector: 'app-lecarstvo',
  templateUrl: './lecarstvo.component.html',
  styleUrls: ['./lecarstvo.component.css']
})
export class LecarstvoComponent implements OnInit {
  
  lecs: SmallLec[] = [];


  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(public lecses:LecDataService,public someS:SomeDataService,private breakpointObserver: BreakpointObserver, public TService: TokenDataService) {}
  //lecs: LecDataService[] = [];
  ngOnInit(): void {
    // axios.post('http://localhost:8080/Users/getLec', {idrec:this.someS.actualrecid, token:this.TService.token }
    // )
    // .then(
    //   (res)=>{console.log( res.data.msg);
    //     this.lecs = res.data.leclist;
    //     console.log( this.lecs);}
    // )

  }
}
