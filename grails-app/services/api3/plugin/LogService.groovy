package api3.plugin

import grails.gorm.transactions.Transactional
import java.time.LocalDate

@Transactional
class LogService {

    def salvarLog(Map dadosLog) {
        Map retorno = [success: true]
        log.error("dadosLog: ${dadosLog}")
        String descricao = montarDescricaoLog(dadosLog)
        Log novoLog = new Log(data: LocalDate.now(), descricao: descricao)
        log.debug("novoLog: ${novoLog.descricao} ${novoLog.data}")

        try {
            log.debug("Save")
            novoLog.save(flush: true)
        } catch (Exception e) {
            log.error("Error | ${e}")
            retorno.success = false
        }

        return retorno
    }

    private static String montarDescricaoLog(Map dadosLog) {
        String operation = dadosLog.operation ?: ''
        String situation = dadosLog.situation ?: ''
        String resource = dadosLog.resource ?: ''
        String resourceId = dadosLog.resourceId ?: ''
        String errors = dadosLog.errors ?: ''

        String descricao = "${situation} no(a) ${operation} do ${resource} de ID ${resourceId}"

        if (situation == 'Failure' && errors) {
            descricao += ": ${errors}"
        }

        return descricao
    }
}
