import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Question} from "../../dto/question";


@Component({
  selector: 'app-question',
  templateUrl: './question.component.html',
  styleUrls: ['./question.component.css']
})
export class QuestionComponent implements OnInit{

  @Input()
  question:Question;
  @Output()
  quest = new EventEmitter<Question>();
  @Input()
  isActive = false;
  checkeds:Array<boolean>;

  isEdit = false;
  @Output()
  deleteQ = new EventEmitter<boolean>();


  constructor() {
    this.question = new Question('', new Array<string>(2), 0)
    this.checkeds = new Array<boolean>(this.question.answers.length)
    this.checkeds.forEach(value => value = false)
  }




  click() {
    console.log(this.question)
  }

  clickAdd() {
    this.question.answers.push("")
    this.checkeds.push(false)
  }

  clickDell(){
    if (this.question.answers.length > 2) {
      this.question.answers.pop()
      this.checkeds.pop()
    }
  }

  trackByFn(index: any, item: any) {
    return index;
  }

  clickNum(i: number) {
    for (let j = 0; j<this.checkeds.length; j++)
      if((i==j) && this.checkeds[j]) this.question.rightAnswer = i;
      else this.checkeds[j] = false;
    // console.log(`${i} ${this.checkeds.toString()}`)
  }

  save(){
    this.quest.emit(this.question)
    if (!this.isEdit) {
      this.checkeds = new Array<boolean>(this.question.answers.length)
    } else {
      this.isEdit = !this.isEdit
      this.isActive = !this.isActive
    }
  }

  ngOnInit(): void {
    this.checkeds[this.question.rightAnswer] = true
  }

  deleteQuestion(){
    this.deleteQ.emit(true)
  }
}
