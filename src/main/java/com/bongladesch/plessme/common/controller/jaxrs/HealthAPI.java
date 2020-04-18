package com.bongladesch.plessme.common.controller.jaxrs;

import com.bongladesch.plessme.common.usecase.ILogger;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
* HealthAPI implements a REST API with JAX-RS as endpoint for health or status checks. These checks
* can e.g. used by Kubernetes for readiness/liveness probes.
*/
@Path("/healthz")
public class HealthAPI {

    private ILogger logger;

    /**
    * Public constructor to inject to logger dependency.
    *
    * @param logger logger instance
    */
    @Inject
    public HealthAPI(ILogger logger) {
        this.logger = logger;
    }

    /**
    * Status reponds with http status 200 if the application is running.
    *
    * @return Http response with 200.
    */
    @GET
    @PermitAll
    public Response status() {
        logger.info("Heath status: ok");
        return Response.ok().build();
    }
}
