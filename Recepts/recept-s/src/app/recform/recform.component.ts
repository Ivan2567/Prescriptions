import { Component, Input, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { TestReceptService } from '../services/test-recept.service'
import { SomeDataService } from '../services/some-data.service';
import { DomSanitizer } from '@angular/platform-browser';
import { Qr } from '../classes/Qr';
//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import axios from 'axios';
import { NgModel } from '@angular/forms';
import * as FileSaver from 'file-saver';
import { Router } from '@angular/router';
import { TokenDataService } from '../services/token-data.service';

@Component({
  selector: 'app-recform',
  templateUrl: './recform.component.html',
  styleUrls: ['./recform.component.css'],
  //providers: [SomeDataService]
})
export class RecformComponent  implements OnInit{
  image : any ; 
  thumbnail: any;
 // showSpinner: boolean = true

  
  // reader = new FileReader().onload = (e) => this.image = e.target.result;

// constructor(private testS: TestReceptService,public someS:SomeDataService) {}
constructor(private testS: TestReceptService,public someS:SomeDataService
  , private sanitizer: DomSanitizer,
  private TService: TokenDataService,
  private router: Router
  ) { }
  // @Input() childMessage?: string;
  // img: string = ""+this.childMessage
  // diagnoz: string = this.someS.actualdiagnoz;
  // status: string = this.someS.actualstatus;
  id : number = 0
  k : number = 0
  objectURL: string = ""
  // name: string = "";
  // secname: string = "";
  //desc: string = "";
  //qr: any = this.someS.QR
  //this.bytes = Qr.bytes
  ngOnInit(): void {
      axios.post('http://localhost:8080/Users/CheckTocken', {token:this.TService.token}
      ) 
      .then(
        (res)=>{
          if(res.data.msg=="Wrong token"){this.router.navigate(['login'])}
              }
      )
    this.id = this.someS.actualrecid;
    axios.post('http://localhost:8080/Users/getQR', {idrec:this.id, token:this.TService.token}
    ) 
    .then(
      (res)=>{
          
          //   let imageBuffer = new Buffer(res.data.length)
          //   for(let b=0; b<res.data.length; b++) {
          //   imageBuffer[b] = res.data[b];
          // }

              //var encodedImage = Base64.encode(res);

              console.log(res)
              this.objectURL = 'data:image/png;base64,' + 
              //  imageBuffer.toString('base64')
               res.data
               //btoa(res.data)
               ;
      
               //this.thumbnail = this.sanitizer.bypassSecurityTrustUrl(objectURL);
               //this.thumbnail = this.someS.QR;
              // const reader = new FileReader();
              // reader.onload = (e) => this.image = e.target?.result;
              // reader.readAsDataURL(new Blob([res.data]));
             
            //});
            }
    )

  }

  qr():void{
  
    this.thumbnail = this.sanitizer.bypassSecurityTrustUrl(this.someS.QR);
  }


  createRec(){
    // this.diagnoz = this.someS.actualdiagnoz;
    // this.status = this.someS.actualstatus;
    // this.testS.polis = this.diagnoz
    // this.testS.surname = this.sname
    // this.testS.name = this.name
    // this.testS.secname = this.secname
    // this.testS.discr = this.desc

    // this.diagnoz ="";
    // this.sname ="";
    // this.name ="";
    // this.secname ="";
    // this.desc ="";
  }
  downloadPDF(){
    axios.post('http://localhost:8080/Users/getPDF', {idrec:this.id, polis:this.TService.polis , token:this.TService.token }
    ) 
    .then(
      (res)=>{
              // const binaryString = window.atob(res.data);
              // const bytes = new Uint8Array(binaryString.length);
              // const mappedData = bytes.map((byte, i) => binaryString.charCodeAt(i));
              // const blob = new Blob([mappedData], { type: 'application/pdf' });
              // FileSaver.saveAs(blob, 'foo.pdf')
              fetch("data:application/pdf;base64," + res.data)
                .then(function(resp) {return resp.blob()})
                .then(function(blob) {
                FileSaver.saveAs(blob, 'foo.pdf')
  });
            }
    )

  }

}
