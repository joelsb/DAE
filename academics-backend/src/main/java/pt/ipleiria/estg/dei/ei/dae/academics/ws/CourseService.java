package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.CourseDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.CourseBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("courses") // relative URL web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class CourseService {

    @EJB
    private CourseBean courseBean;

    // Converts an entity Course to a DTO CourseDTO class
    private CourseDTO toDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();

        courseDTO.setCode(course.getCode());
        courseDTO.setName(course.getName());

        return courseDTO;
    }


    // Converts an entire list of entities into a list of DTOs
    private List<CourseDTO> toDTOs(List<Course> courses) {
        return courses.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative URL path is “/courses/”
    public List<CourseDTO> getAllCourses() {
        return toDTOs(courseBean.getAll());
    }

    @POST
    @Path("/")
    public Response createNewCourse(CourseDTO courseDTO) {
        courseBean.create(courseDTO.getCode(), courseDTO.getName());
        Course newCourse = courseBean.find(courseDTO.getCode());
        if (newCourse == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.CREATED).entity(toDTO(newCourse)).build();
    }
    @PUT
    @Path("/")
    public Response updateCourse(CourseDTO courseDTO) {
        Course course = courseBean.find(courseDTO.getCode());
        if (course == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else{
            courseBean.update(courseDTO.getCode(), course.getName());
            return Response.status(Response.Status.OK).entity(toDTO(course)).build();
        }
    }
    @DELETE
    @Path("/{code}")
    public Response deleteCourse(@PathParam("code") long code) {
        Course course = courseBean.find(code);
        if (course == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else{
            courseBean.remove(code);
            return Response.status(Response.Status.OK).entity(toDTO(course)).build();
        }
    }
}
