import { Component, ElementRef, Inject, ViewChild } from '@angular/core';
import { SomeDataService } from '../services/some-data.service';
import { OtchetdataService } from '../services/otcetdata.service';
import { LecDataService } from '../services/lec-data.sevice';
import {FormGroup, FormControl} from '@angular/forms';
import axios from 'axios';
import { TokenDataService } from '../services/token-data.service';
import { DomSanitizer } from '@angular/platform-browser';
import { DOCUMENT } from '@angular/common';
import { Router } from '@angular/router';
//import { Otch } from '../classes/Otch';

@Component({
  selector: 'app-otchetnost',
  templateUrl: './otchetnost.component.html',
  styleUrls: ['./otchetnost.component.css']
})
export class OtchetnostComponent {
  @ViewChild('myDiv')
  myDiv!: ElementRef;
  range = new FormGroup({
    start: new FormControl(null),
    end: new FormControl(null),
  });
  startdate:String = ""
  enddate:String = ""
  PathReportString: string = "";
  
  constructor(public lecses:LecDataService,public serV: SomeDataService, public otchserv: OtchetdataService, private TService: TokenDataService, private sanitizer: DomSanitizer,
    private router: Router
    ) {}
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
  create(){
     this.otchserv.startdate = this.range.get('start')?.value ;
    this.otchserv.enddate = this.range.get('end')?.value;
    this.startdate = this.range.get('start')?.value ;
    this.enddate = this.range.get('end')?.value ;
    // this.otchserv.startdate = this.startdate.substring(4,15)
    // this.otchserv.enddate = this.enddate.substring(4,15)
    axios.post('http://localhost:8080/Users/getOtchet',{startdate: this.startdate, enddate:this.enddate, polis:this.TService.polis, token:this.TService.token}) 
    .then(
      (res)=>{
        this.otchserv.startdate = res.data.startdate;
        this.otchserv.enddate = res.data.enddate;
        //this.router.navigate(['history'])
        this.PathReportString = 'data:application/pdf;base64,' + (this.sanitizer.bypassSecurityTrustResourceUrl(res.data) as any).changingThisBreaksApplicationSecurity;
        //document.getElementById('ifrm').setAttribute("src", this.PathReportString);
        this.myDiv.nativeElement.setAttribute("src", this.PathReportString);
        });
  }
  
  close(){
    this.serV.data = !this.serV.data;
    this.serV.QR = ""
    this.serV.actualrecid = 0
    this.lecses.lecss = []
  }

}
