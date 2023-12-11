import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Question} from "../../dto/question";
import {StorageService} from "../../services/storage.service";


@Component({
  selector: 'app-question',
  templateUrl: './question.component.html',
  styleUrls: ['./question.component.scss']
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

  @Input()
  num:number=0;

  notReady = false;

  @Input()
  isManyAnswer=false

  constructor() {
    this.question = new Question('', new Array<string>(2), new Array<number>(1))
    this.checkeds = new Array<boolean>(this.question.answers.length)
    this.checkeds.forEach(value => value = false)
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
    if (this.isManyAnswer) {
      if(this.checkeds[i]) this.question.rightAnswer.push(i)
      else this.question.rightAnswer = this.question.rightAnswer.filter((item => item !== i))
    }else
      for (let j = 0; j<this.checkeds.length; j++)
      if((i==j) && this.checkeds[j]) this.question.rightAnswer[0] = i;
      else this.checkeds[j] = false;
  }

  save(){
    if(this.question.text !== '' && this.question.rightAnswer[0] > -1
        && this.question.answers[0] !=='' && this.question.answers[1] !=='' && this.lessHundred) {
        this.notReady = false
        this.quest.emit(this.question)
      if (!this.isEdit) {
        this.checkeds = new Array<boolean>(this.question.answers.length)
      } else {
        this.isEdit = !this.isEdit
        this.isActive = !this.isActive
      }
    } else {
      this.notReady = true
    }
  }

  ngOnInit(): void {
    this.question.rightAnswer.forEach (value =>
      this.checkeds[value] = true)
  }

  deleteQuestion(){
    this.deleteQ.emit(true)
  }

  get lessHundred():boolean{
    return this.question.answers.every(value => value.length<100)
  }

}
