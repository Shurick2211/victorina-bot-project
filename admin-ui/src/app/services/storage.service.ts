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

  public isAdmin = false

  constructor(private api:ApiService) {
    this.victorinas = new Array<Victorina>()
  }

  refreshPerson(){
    if (this.userId !== null)
    this.api.getPerson(this.userId, this.userId).subscribe(response => {
      this.person = response.body
      console.log(this.person?.userName)
      this.isAdmin = this.person?.role === PersonRole.ADMIN
    })
  }

  refreshVictorins(){
    console.log(`Is admin = ${this.isAdmin}`)

    if (this.isAdmin)
      this.api.getAllVictorinas().subscribe(response => {
        this.victorinas = response.reverse()
      })
    else this.api.getAllUserVictorinas(this.person!!.id).subscribe(
      response =>
        this.victorinas = response.reverse()
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
