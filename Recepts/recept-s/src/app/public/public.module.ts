import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { MatCardModule } from '@angular/material/card';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { PublicRoutingModule } from './public-routing.module';



@NgModule({
  declarations: [
    // Own Components
    //LoginComponent,
    //RegisterComponent
  ],
  imports: [
    PublicRoutingModule,
  ]
})
export class PublicModule { }
