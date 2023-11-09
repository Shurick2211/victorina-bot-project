import { Component } from '@angular/core';
import {Question} from "../../dto/question";


@Component({
  selector: 'app-question',
  templateUrl: './question.component.html',
  styleUrls: ['./question.component.css']
})
export class QuestionComponent {
  question:Question;


  constructor() {
    this.question = new Question('', new Array<string>(2), 0)
  }


  click() {
    console.log(this.question)
  }

  clickAdd() {
    this.question.answers.push("")
  }

  trackByFn(index: any, item: any) {
    return index;
  }

  clickNum(i: number) {
    this.question.rightAnswer = i
  }
}
