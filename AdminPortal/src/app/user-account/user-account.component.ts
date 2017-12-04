import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../user.service';

@Component({
  selector: 'app-user-account',
  templateUrl: './user-account.component.html',
  styleUrls: ['./user-account.component.css']
})
export class UserAccountComponent implements OnInit {

  userList: Object[];

  constructor(private userService: UserService, private router: Router) {
    this.getUsers();
  }

  getUsers() {
    this.userService.getUsers().subscribe(
      res => {
        console.log("res = " + res);
        let str1 = JSON.stringify(res);
        console.log("str1 = "+str1);
        let str2 = JSON.parse(str1);
        console.log("========================str2="+str2._body);
        this.userList = JSON.parse(str2._body);
        console.log(this.userList);
      },
      error => console.log(error)
    )
  }

  onSelectPrimary(username: string) {
    this.router.navigate(['/primaryTransaction ', username]);
  }

  onSelectSavings(username: string) {
    this.router.navigate(['/savingsTransaction ', username]);
  }

  enableUser(username: string) {
    this.userService.enableUser(username).subscribe();
    location.reload();
  }

  disableUser(username: string) {
    this.userService.disableUser(username).subscribe();
    location.reload();
  }

  ngOnInit() {
  }

}
