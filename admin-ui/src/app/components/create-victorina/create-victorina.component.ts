import {Component, OnInit} from '@angular/core';
import {Victorina} from "../../dto/victorina";

@Component({
  selector: 'app-create-victorina',
  templateUrl: './create-victorina.component.html',
  styleUrls: ['./create-victorina.component.css']
})
export class CreateVictorinaComponent implements OnInit{

  victorina: Victorina;

  info:string = ''

  constructor() {
    this.victorina = new Victorina();
  }

  ngOnInit(): void {

  }


  display() {
    this.info = this.victorina.toString()
  }
}
