package api3.plugin

import grails.gorm.annotation.Entity
import java.time.LocalDate

@Entity
class Log {

    Long id
    LocalDate data
    String descricao

    static mapping = {
        id generator: 'increment'
        version false
    }

    static constraints = {
        id unique: true
        data nullable: false
        descricao maxSize: 1000
    }
}