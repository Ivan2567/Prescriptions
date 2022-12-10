import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs";
import { map } from 'rxjs/operators';
import { WsService } from "./ws.service";

const CHAT_URL = "ws://localhost:8080/ws/chat/room1/patient";
export interface Message {
  author: string;
  message: string;
}

@Injectable()
export class WshatService {
  public messages: Subject<Message>;

  constructor(wsService: WsService) {
    this.messages = <Subject<Message>>wsService.connect(CHAT_URL).pipe(map(
      (response: MessageEvent): Message => {
        let data = JSON.parse(response.data);
        return {
          author: data.author,
          message: data.message
        };
      }
    ));
  }
}