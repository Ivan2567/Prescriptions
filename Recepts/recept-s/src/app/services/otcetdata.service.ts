import { Injectable } from "@angular/core";
import { Otch } from '../classes/Otch';
import { Otchet } from '../classes/Otchet';
import { Otchetdata } from '../classes/Otchetdata';

import { DomSanitizer } from '@angular/platform-browser';
import axios from "axios";
import { Qr } from "../classes/Qr";

@Injectable({providedIn: 'root' })
export class OtchetdataService{
    // data: boolean = false
    // actualrecid: number = 0
    startdate?: String 
    enddate?: String 
    otch: Otch[] = []
    otchet: Otchet[] = []
    otchdata: Otchetdata[] = []
}