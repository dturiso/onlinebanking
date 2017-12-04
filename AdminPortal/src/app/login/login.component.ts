import { Component, OnInit } from '@angular/core';
import {Observable}  from 'rxjs/Observable';
import {LoginService} from '../login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loggedIn: boolean = false;
  username: string;
  password: string;

	constructor (private loginService: LoginService) {
    console.log("LoginComponent.constructor().before: loggedIn = " + this.loggedIn);
    console.log("LoginComponent.constructor().before: localStorage = " + localStorage.getItem('PortalAdminHasLoggedIn'));
    if( localStorage.getItem('PortalAdminHasLoggedIn') == '' ||
        localStorage.getItem('PortalAdminHasLoggedIn') == null) {
      this.loggedIn = false;
    } else {
      this.loggedIn = true;
    }
    console.log("LoginComponent.constructor().after: loggedIn = " + this.loggedIn);
    console.log("LoginComponent.constructor().after: localStorage = " + localStorage.getItem('PortalAdminHasLoggedIn'));
  }

  onSubmit() {
    console.log("LoginComponent.constructor(): onSubmit = " + this.loggedIn);
  	this.loginService.sendCredential(this.username, this.password).subscribe(
      res => {
        this.loggedIn=true;
        localStorage.setItem('PortalAdminHasLoggedIn', 'true');
        location.reload();
        console.log("LoginComponent.onSubmit(): res OK => loggedIn set to true ");
      },
      err => console.log(err)
    );
  }

  ngOnInit() {}

}
