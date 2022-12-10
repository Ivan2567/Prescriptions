import { Injectable } from "@angular/core";
import { DomSanitizer } from '@angular/platform-browser';
import axios from "axios";
import { Qr } from "../classes/Qr";

@Injectable({providedIn: 'root' })
export class SomeDataService{
    data: boolean = false
    actualrecid: number = 0
    actualdiagnoz: string = ""
    actualstatus: string = ""
    actualdatef: string = ""
    actualsrok: number = 0
    fio: string = ""
    QR :any = ''
    constructor(private sanitizer: DomSanitizer) { }
    // qr():void{
    //     axios.post('http://localhost:8080/Users/getQR', {idrec: this.actualrecid}
    //     ) 
    //     .then(
    //       (res)=>{
    //          let thumbnail: any;
    //           //   let imageBuffer = new Buffer(res.data.length)
    //           //   for(let b=0; b<res.data.length; b++) {
    //           //   imageBuffer[b] = res.data[b];
    //           // }
    
    //               //var encodedImage = Base64.encode(res);
    //               console.log(res)
    //                let objectURL = 'data:image/png;base64,' + 
    //               //  imageBuffer.toString('base64')
    //                res.data
    //                //btoa(res.data)
    //                ;
          
    //                this.QR = this.sanitizer.bypassSecurityTrustUrl(objectURL);
    //               // const reader = new FileReader();
    //               // reader.onload = (e) => this.image = e.target?.result;
    //               // reader.readAsDataURL(new Blob([res.data]));
                 
    //             //});
    //             }
    //     )
    //   }
}