import {Injectable} from '@angular/core';
import {Victorina} from "../dto/victorina";
import {ApiService} from "./api.service";
import {Person} from "../dto/person";
import {HttpStatusCode} from "@angular/common/http";
import {PersonRole} from "../utils/person-role";


@Injectable({
  providedIn: 'root'
})
export class StorageService {

  public victorinas:Victorina[];

  public userId:string | null = null

  public person:Person | null = null

  public isReg = false

  constructor(private api:ApiService) {
    this.victorinas = new Array<Victorina>()
  }

  refreshPerson(){
    if (this.userId !== null)
    this.api.getPerson(this.userId).subscribe( response => {
      this.person = response.body
      console.log(this.person?.userName)
    })
  }

  refreshVictorins(){
    console.log(`Is admin = ${this.person!!.role === PersonRole.ADMIN}`)

    if (this.person!!.role === PersonRole.ADMIN)
      this.api.getAllVictorinas().subscribe(response => {
        this.victorinas = response
        console.log(this.victorinas)
      })
    else this.api.getAllUserVictorinas(this.person!!.id).subscribe(
      response =>
        this.victorinas = response
    )
  }

  save(victorina: Victorina ){
    this.api.createVictorina(victorina).subscribe(
      response => {
        console.log(response.status)
      })
    this.refreshVictorins()
  }

  delete(i: number) {
    this.api.deleteVictorina(`${this.victorinas[i].id}`).subscribe(response => {
      console.log(response)    })
    this.refreshVictorins()
  }

  savePerson(){
    this.api.savePassFromPerson(this.person!.id, this.person!.password).subscribe(response => {
      console.log(response)
      if(response.status == HttpStatusCode.Ok) this.isReg = true
    })
  }

}
