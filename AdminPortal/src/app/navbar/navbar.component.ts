import { Component, OnInit } from '@angular/core';
import { LoginService } from '../login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  loggedIn: boolean = false;

	constructor(private loginService: LoginService, private router : Router) {
    console.log("NavbarComponent.constructor().before: loggedIn = " + this.loggedIn);
    console.log("NavbarComponent.constructor().before: localStorage = " + localStorage.getItem('PortalAdminHasLoggedIn'));

    if(localStorage.getItem('PortalAdminHasLoggedIn') == '' ||
       localStorage.getItem('PortalAdminHasLoggedIn') == null) {
			this.loggedIn = false;
		} else {
			this.loggedIn = true;
    }

    console.log("NavbarComponent.constructor().after: loggedIn = " + this.loggedIn);
    console.log("NavbarComponent.constructor().after: localStorage = " + localStorage.getItem('PortalAdminHasLoggedIn'));

	}

	logout(){
		this.loginService.logout().subscribe(
			res => {
				localStorage.setItem('PortalAdminHasLoggedIn', '');
			},
			err => console.log(err)
			);
		location.reload();
		this.router.navigate(['/login']);
	}

	getDisplay() {
    if(!this.loggedIn){
      return "none";
    } else {
      return "";
    }
  }

  ngOnInit() {
  }

}
