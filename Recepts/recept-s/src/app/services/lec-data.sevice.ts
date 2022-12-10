import { Injectable } from "@angular/core";
import { SmallLec } from '../classes/SmallLec';

@Injectable({providedIn: 'root' })
export class LecDataService{
    lecss: SmallLec[] = []
}