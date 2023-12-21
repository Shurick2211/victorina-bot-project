import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Victorina} from "../../dto/victorina";
import {Question} from "../../dto/question";
import {StorageService} from "../../services/storage.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {EmojiComponent, EmojiModule} from "@ctrl/ngx-emoji-mart/ngx-emoji";
import {PickerComponent} from "@ctrl/ngx-emoji-mart";



@Component({
  selector: 'app-create-victorina',
  templateUrl: './create-victorina.component.html',
  styleUrls: ['./create-victorina.component.scss']
})
export class CreateVictorinaComponent implements OnInit{

  @Input()
  title=''

  @Input()
  victorina: Victorina;
  q:Question ;
  isNotReady = false;
  num = undefined;

 endDate:Date | null = null
 startDate: Date | null = null



  constructor(protected storage:StorageService, activeRoute:ActivatedRoute, private router:Router, private dialog: MatDialog) {
    this.num = activeRoute.snapshot.params['id']
    if (!this.num) {
      this.victorina = this.newVictorina()
      this.title = 'Create new victorina!'
    } else {
      this.victorina = storage.victorinas[this.num]
        if (this.victorina.startDate !== null) this.startDate = new Date(this.victorina.startDate)
        if (this.victorina.endDate !== null) this.endDate = new Date(this.victorina.endDate)
      this.title = 'Edit victorina!'
      this.victorina.isManyAnswer = this.victorina.questions[0].rightAnswer.length > 1
    }
    this.q = new Question('',null,['',''],Array.of<number>(-1))
  }

  ngOnInit(): void {
    if(this.title.length < 1) {
      this.title = 'Create new victorina!'
    }
  }


  addToInfo(q:Question){
    this.victorina.questions.push(q)
    this.q = new Question('',null,['',''], Array.of<number>(-1))
    this.clicManyAnswers()
  }

  editToInfo(q: Question, i: number){
    this.victorina.questions[i] = q

  }

  deleteQuestion(i: number) {
    console.log(i)
    this.victorina.questions.splice(i--,1)
  }

  save() {
    if (this.victorina.questions.length > 0 && this.victorina.name.length > 0 &&
        this.startDate !== null && this.endDate !== null
    ) {
      this.victorina.startDate = this.startDate
      let end = new Date(this.endDate)
          end.setDate(this.endDate.getDate() + 1)
      this.victorina.endDate = end
      this.isNotReady = false
      console.log(this.victorina.toString())
      this.storage.save(this.victorina)
      this.victorina = this.newVictorina()
      this.router.navigateByUrl('/create')
    } else this.isNotReady = true

  }

  clicManyAnswers(){
    if (this.victorina.isManyAnswer)
      this.q.rightAnswer = new Array<number>()
    else  this.q.rightAnswer = Array.of<number>(-1)
  }

  newVictorina():Victorina{
    return  new Victorina(null,'','',
      new Array<Question>(), this.storage.person!.id,null,
      null, null, false, null,false,
      false,null,null);
  }



  openEmojiModal(): void {
    const buttonElement = document.getElementById('emoji')!
    const buttonRect = buttonElement.getBoundingClientRect()
    const emojiWin =
    this.dialog.open(PickerComponent, {
      width: '25%',
      height: '35%',
      position:{
        top: buttonRect.bottom + 'px',
        left: buttonRect.left + 'px',
      }
    });
    emojiWin.componentInstance.emojiSelect.subscribe(
      em=>this.addEmoji(em)
    )
  }

  cursorOnText: number|undefined

  addEmoji(event:any) {
    const emoji = event.emoji.native
    console.log(this.cursorOnText)
    this.victorina.title = this.victorina.title.slice(0, this.cursorOnText)
      + emoji + this.victorina.title.slice(this.cursorOnText)
  }

  onFocus() {
    this.dialog.closeAll()
  }

  italic() {

  }

  bold() {

  }


  onInput($event: Event) {
    const textarea = $event.target as HTMLTextAreaElement
    this.cursorOnText = textarea.selectionStart;
    console.log(this.cursorOnText)
  }
}
