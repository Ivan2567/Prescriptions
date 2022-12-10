import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import axios from 'axios';
import { TokenDataService } from '../../services/token-data.service';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
// import { AuthService } from '../../services/auth-service/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  loginForm: FormGroup = new FormGroup({
    polis: new FormControl(null, [Validators.required]),
    password: new FormControl(null, [Validators.required]),
  });

  constructor(
    //private authService: AuthService,
    private TService: TokenDataService,
    private router: Router
  ) { }

  login() {
    // if (!this.loginForm.valid) {
    //   return;
    // }
    // this.authService.login(this.loginForm.value).pipe(
    //   // route to protected/dashboard, if login was successfull
    //   tap(() => this.router.navigate(['../../protected/dashboard']))
    // ).subscribe();
    axios.post('http://localhost:8080/Users/Login',{polis:this.loginForm.get('polis')?.value, pass:this.loginForm.get('password')?.value})
    .then(
      (res)=>{console.log( res.data.token);
        this.TService.token = res.data.token
        this.TService.f = res.data.f
        this.TService.i = res.data.i
        this.TService.o = res.data.o
        this.TService.polis = res.data.polis
        //this.TService.pass = res.data.pass
        this.TService.email = res.data.email
        console.log( this.TService.token);
        this.router.navigate(['main/history'])
        });
  }

}