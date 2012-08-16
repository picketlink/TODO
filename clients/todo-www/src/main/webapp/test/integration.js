(function( $ ) {

var pipeline = aerogear.pipeline([
        {
            name: "tasks",
            settings: {
                url: "/todo-server/task"
            }
        },
        {
            name: "projects",
            settings: {
                url: "/todo-server/project"
            }
        },
        {
            name: "tags",
            settings: {
                url: "/todo-server/tag"
            }
        }
    ]),
    Tasks = pipeline.pipes.tasks,
    Projects = pipeline.pipes.projects,
    Tags = pipeline.pipes.tags;

module( "TODO Project" );
asyncTest( "create, modify and delete", function() {
    expect( 6 );

    Projects.save({
        title: "New Project",
        style: "project-255-0-0"
    },
    {
        ajax: {
            success: function( data, textStatus, jqXHR ) {
                equal( data.title, "New Project", "Project title correct" );
                equal( data.style, "project-255-0-0", "Project style correct" );

                Projects.save({
                    id: data.id,
                    title: "Modified Project"
                },
                {
                    ajax: {
                        success: function( data, textStatus, jqXHR ) {
                            equal( data.title, "Modified Project", "Project title correct" );
                            equal( data.style, "project-255-0-0", "Project style unchanged" );

                            Projects.del({
                                record: data.id,
                                ajax: {
                                    success: function( data, textStatus, jqXHR ) {
                                        equal( textStatus, "success", "Project deleted" );
                                        ok( data && data.tasks && data.tasks.length >= 0, "Task list returned");
                                        start();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    });
});

})( jQuery );