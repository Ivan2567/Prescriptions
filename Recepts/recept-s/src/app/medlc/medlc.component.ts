import { Component } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { TokenDataService } from '../services/token-data.service';
import { Router } from '@angular/router';
import axios from 'axios';


@Component({
  selector: 'app-medlc',
  templateUrl: './medlc.component.html',
  styleUrls: ['./medlc.component.css']
})
export class MedlcComponent {

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(private breakpointObserver: BreakpointObserver, public TService: TokenDataService,
    private router: Router) {}
    ngOnInit(): void {
      axios.post('http://localhost:8080/Users/CheckTocken', {token:this.TService.token}
      ) 
      .then(
        (res)=>{
          if(res.data.msg=="Wrong token"){this.router.navigate(['login'])}
              }
      )
    }
}
