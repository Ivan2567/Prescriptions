import { Component, ElementRef } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { WsService } from "../services/ws.service";
import { WshatService } from "../services/wschat.service";


@Component({
  selector: 'app-wstest',
  templateUrl: './wstest.component.html',
  styleUrls: ['./wstest.component.css'],
  providers: [WsService, WshatService]
})
export class WstestComponent {

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

    
  constructor(private breakpointObserver: BreakpointObserver, private chatService: WshatService) {
    
  chatService.messages.subscribe((msg) => {
      console.log("Response from websocket: " + msg.message);
      this.insert(msg.message)
    });
  }

  public message = {
    author: "patient",
    message: " "
  };

  sendMsg() {
    this.message.message = (<HTMLInputElement> document.getElementById("message")).value;
    console.log("new message from client to websocket: ", this.message);
    this.chatService.messages.next(this.message);
    this.insert(this.message.message)
    this.message.message = "";
    (<HTMLInputElement> document.getElementById("message")).value = "";
  }
  

 
  //Helper function for inserting HTML as the first child of an element
  insert( message:any) {
    (<HTMLInputElement> document.getElementById("chat")).insertAdjacentHTML("afterbegin", "<p>" + message + "</p>");
  }
  
  //Helper function for selecting element by id
  id(id:any) {
      return document.getElementById(id);
  }
}
