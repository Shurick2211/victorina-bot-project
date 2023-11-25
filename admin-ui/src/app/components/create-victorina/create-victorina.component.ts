import {Component, Input, OnInit} from '@angular/core';
import {Victorina} from "../../dto/victorina";
import {Question} from "../../dto/question";
import {StorageService} from "../../services/storage.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FormControl, FormGroup} from "@angular/forms";


@Component({
  selector: 'app-create-victorina',
  templateUrl: './create-victorina.component.html',
  styleUrls: ['./create-victorina.component.css']
})
export class CreateVictorinaComponent implements OnInit{
  @Input()
  title=''

  @Input()
  victorina: Victorina ;
  q:Question ;
  isNotReady = false;
  num = undefined;


  today = new Date()
  month = this.today.getMonth();
  year = this.today.getFullYear();


  constructor(private storage:StorageService, private activeRoute:ActivatedRoute, private router:Router) {
    console.log(`${this.month} : ${this.year}`)


    this.num = activeRoute.snapshot.params['id']
    if (!this.num) {
      this.victorina = new Victorina(null,'','', new Array<Question>(), '','');
      this.title = 'Create new victorina!'
    } else {
      this.victorina = storage.victorinas[this.num]
      this.title = 'Edit victorina!'
    }
    this.q = new Question('',['',''],-1)
  }

  ngOnInit(): void {
    if(this.title.length < 1) {
      this.title = 'Create new victorina!'
    }
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
    if (this.victorina.questions.length > 0 && this.victorina.name.length > 0) {
      this.isNotReady = false
      console.log(this.victorina.toString())
      this.storage.save(this.victorina)
      this.victorina = new Victorina(null,'','', new Array<Question>(), '','');
      this.router.navigateByUrl('/create')
    } else this.isNotReady = true

  }


  campaignOne = new FormGroup({
    start: new FormControl(new Date(this.year, this.month, this.today.getDay())),
    end: new FormControl(new Date(this.year, this.month, this.today.getDay() + 2)),
  });



}
