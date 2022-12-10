import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import axios from 'axios';
import { TokenDataService } from '../../services/token-data.service';
//import { CustomValidators } from '../../custom-validator';
//import { AuthService } from '../../services/auth-service/auth.service';
import { tap } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  registerForm = new FormGroup({
    polis: new FormControl(null, [Validators.required]),
    email: new FormControl(null, [Validators.required, Validators.email]),
    username: new FormControl(null, [Validators.required]),
    firstname: new FormControl(null, [Validators.required]),
    lastname: new FormControl(null, [Validators.required]),
    password: new FormControl(null, [Validators.required]),
    passwordConfirm: new FormControl(null, [Validators.required])
  })
  polis2:string = this.registerForm.get('polis')?.value
  constructor(
    private router: Router,
    private TService: TokenDataService
   
  ) { }

  register() {
   axios.post('http://localhost:8080/Users/Register',{polis:this.registerForm.get('polis')?.value, f:this.registerForm.get('username')?.value, i:this.registerForm.get('firstname')?.value, o:this.registerForm.get('lastname')?.value, email:this.registerForm.get('email')?.value, pass:this.registerForm.get('password')?.value})
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
        
        //this.router.navigate(['history'])
      }
    }
