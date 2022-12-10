
import { Component, EventEmitter, OnInit, Output, ViewChild,Input } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { SomeDataService } from '../services/some-data.service';
import { LecDataService } from '../services/lec-data.sevice';
import { OtchetdataService } from '../services/otcetdata.service';
import { TestReceptService } from '../services/test-recept.service';
import { TokenDataService } from '../services/token-data.service';
import { SmallRec } from '../classes/SmallRec';
import { SmallLec } from '../classes/SmallLec';
import axios from 'axios';
import { RecformComponent } from '../recform/recform.component';
import { Router } from '@angular/router';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-otchetcard',
  templateUrl: './otchetcard.component.html',
  styleUrls: ['./otchetcard.component.css']
})
export class OtchetcardComponent {

  message: string = ""
  @Output() messageEvent = new EventEmitter<string>();
  cards: SmallRec[] = [];
  allcards: SmallRec[] = [];
  activecards: SmallRec[] = [];
  disactivecards: SmallRec[] = [];
  color:string = ""  
  filter1: boolean = true;
  filter2: boolean = false;
  
  @Input() filter: string = '';
  

  
  @ViewChild(RecformComponent) recformcomponent?: RecformComponent
  constructor(public lecses:LecDataService, private serV: SomeDataService, private sanitizer: DomSanitizer, private TService: TokenDataService, private router: Router,  public otchserv: OtchetdataService ) {}
  // lecs: LecDataService[] = []
  open(actualrecid:number, actualdiagnoz:string, actualstatus:string, actualdatef:string, actualsrok:number){ 
    this.serV.data = !this.serV.data;
    this.serV.actualrecid = actualrecid;
    this.serV.actualdiagnoz = actualdiagnoz;
    this.serV.actualstatus = actualstatus;
    this.serV.actualdatef = actualdatef;
    this.serV.actualsrok = actualsrok;
    axios.post('http://localhost:8080/Users/getQR', {idrec:this.serV.actualrecid}
    ) 
    .then(
      (res)=>{
              console.log(this.serV.actualrecid)
              console.log(res)
              this.message = 'data:image/png;base64,' + res.data;
              console.log(this.message)
              this.serV.QR = (this.message)
            }
    )
    axios.post('http://localhost:8080/Users/getLec', {idrec:this.serV.actualrecid }
    )
    .then(
      (res)=>{console.log( res.data.msg);
        this.lecses.lecss = res.data.leclist;
        console.log( res.data.leclist);
      }
    )
  }
  ngOnInit(): void {
    axios.post('http://localhost:8080/Users/getPac', {polis:"0000000000000000", filter:[], token:this.TService.token}
    // ,{ headers: {"Access-Control-Allow-Origin" : "http://localhost:8080/Users/getPac" }}
    )
    .then(
      (res)=>{console.log( res.data.msg);
        if(res.data.msg == 'ok'){
          this.cards = res.data.recepts;
                  this.cards.forEach(element => {
                    if(element.status == 'активен'){
                      this.activecards.push(element)
                      this.allcards.push(element)
                    } else { 
                      this.disactivecards.push(element)
                      this.allcards.push(element)
                    }});

        } else { 
          this.router.navigate(['login'])
        }
        console.log( this.cards);
      }
    )
  }
}
