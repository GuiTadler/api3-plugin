package api3.plugin

import grails.gorm.transactions.Transactional
import java.time.LocalDate
import org.grails.web.json.JSONObject
import javax.servlet.http.HttpServletRequest

@Transactional
class LogService {

    void salvarLog(HttpServletRequest request, JSONObject response, LocalDate data) {
        if (request.method != 'GET') {
            Log log = criarLog(request, response, data)
            log.save(flush: true)
        }
    }

    private static Log criarLog(HttpServletRequest request, JSONObject response, LocalDate data) {
        String operation = getOperation(request)
        String situation = getSituation(response)
        String resource = getResource(request)
        String resourceId = request.getParameter("id") ?: response?.data?.id ?: ""

        String descricao = "${situation} na ${operation} do recurso ${resource} do ID ${resourceId}"
        if (situation == 'Failure') {
            descricao += ": ${getErrors(response)}"
        }

        return new Log(data: data, descricao: descricao)
    }

    private static String getOperation(HttpServletRequest request) {
        switch (request.method) {
            case 'POST':
                return 'Creat'
            case 'PUT':
                return 'Update'
            default:
                return 'Delete'
        }
    }

    private static String getSituation(JSONObject response) {
        if (response?.message || response?.errors) {
            return 'Failure'
        }
        return 'Success'
    }

    private static String getResource(HttpServletRequest request) {
        String resource = request.servletPath
        resource = resource.substring(resource.indexOf("/") + 1)
        resource = resource.substring(0, resource.indexOf("/"))

        return resource()
    }

    private static String getErrors(JSONObject response) {
        if (!response.errors) {
            return response.message
        }
        def errors = response.errors.collect { error ->
            "${error.field}: ${error.message}"
        }
        return errors()
    }
}
