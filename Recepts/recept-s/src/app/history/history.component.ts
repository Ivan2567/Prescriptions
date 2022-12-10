import { Component, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { SomeDataService } from '../services/some-data.service';
import { LecDataService } from '../services/lec-data.sevice';
import { TokenDataService } from '../services/token-data.service';

import axios from 'axios';
import { Router } from '@angular/router';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent {
  reader = new FileReader();
  filter:string = '3';
  
  constructor(public lecses:LecDataService,public serV: SomeDataService,
    private TService: TokenDataService,
    private router: Router) {}
  // message: string | undefined ;
  // receiveMessage($event: string | undefined) {
  //   this.message = $event
  //   console.log(this.message)
  // }
  ngOnInit(): void {
    axios.post('http://localhost:8080/Users/CheckTocken', {token:this.TService.token}
    ) 
    .then(
      (res)=>{
        if(res.data.msg=="Wrong token"){this.router.navigate(['login'])}
            }
    )
  }

  
  close(){
    this.serV.data = !this.serV.data;
    this.serV.QR = ""
    this.serV.actualrecid = 0
    this.lecses.lecss = []
  }

}
