package server;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

@Path(value = "Files")
@Produces(value = "application/json")
public class FileRepository {

    @POST
    @Path("single")
    @Consumes("multipart/form-data")
    @Produces("application/json")
    public Response postFile(@Multipart("id") Integer id, @Multipart("upfile") Attachment attachment) throws IOException {
        String filename = attachment.getContentDisposition().getParameter("filename");
        String fileExtension = FilenameUtils.getExtension(filename);
        if (Utils.isExtensionAllowed(fileExtension)) {
            InputStream in = attachment.getObject(InputStream.class);
            FileUtils.copyInputStreamToFile(in, FileUtils.getFile(filename));
            String entity = Utils.isImageExtension(fileExtension) ? "Imagen" : "Video";
            return Response.ok("{\"status\": \"Copied!\", \"type\": \"" + entity + "\"}").build();
        } else {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"reason\": \"Extensión inválida!\"}")
                    .build();
        }
    }
}
