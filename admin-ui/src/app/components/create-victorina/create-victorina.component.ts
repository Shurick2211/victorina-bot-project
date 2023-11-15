import {Component, Input, OnInit} from '@angular/core';
import {Victorina} from "../../dto/victorina";
import {Question} from "../../dto/question";
import {ApiService} from "../../services/api.service";


@Component({
  selector: 'app-create-victorina',
  templateUrl: './create-victorina.component.html',
  styleUrls: ['./create-victorina.component.css']
})
export class CreateVictorinaComponent implements OnInit{

  @Input()
  victorina: Victorina;
  q:Question;


  constructor(private httpService:ApiService) {
    this.victorina = new Victorina('','','', new Array<Question>(), '','');
    this.q = new Question('',['',''],-1)
  }

  ngOnInit(): void {

  }


  addToInfo(q:Question){
    this.victorina.questions.push(q)
    this.q = new Question('',new Array<string>(q.answers.length), -1)
  }

  editToInfo(q: Question, i: number){
    this.victorina.questions[i] = q

  }

  deleteQuestion(i: number) {
    console.log(i)
    this.victorina.questions.splice(i--,1)
  }

  save() {
    console.log(this.victorina.toString())
    this.httpService.createVictorina(this.victorina).subscribe(
      response => {
        console.log(response.status)
      })
  }
}
