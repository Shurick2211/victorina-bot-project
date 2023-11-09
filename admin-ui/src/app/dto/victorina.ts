import {Question} from "./question";
import {Input, Output} from "@angular/core";

export class Victorina {
   name:string;
   title:string;
   questions:Array<Question>;

  constructor() {
    this.name = '';
    this.title = '';
    this.questions = new Array<Question>();

  }
  // constructor(name:string, title:string, questions:Array<Question>) {
  //   this._name = name;
  //   this._title = title;
  //   this._questions = questions;
  // }
  //
  toString():string{
    return `Victorina[name=${this.name}; title=${this.title}; question=${this.questions}]`
  }

}
