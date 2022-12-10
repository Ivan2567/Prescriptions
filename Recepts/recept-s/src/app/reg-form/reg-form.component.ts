import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-reg-form',
  templateUrl: './reg-form.component.html',
  styleUrls: ['./reg-form.component.css']
})
export class RegFormComponent implements OnInit {
  constructor() { }
  loginForm: any = {
    login: '',
    password: '',
  }
  ngOnInit(): void {}
  printForm(){
    console.log(this.loginForm);
}
  loginfo(event : any)
  {
      const log = event.target.value
      this.loginForm.login = log
  }
  pasinfo(event : any)
  {
      const pas = event.target.value
      this.loginForm.password = pas
  }
}
