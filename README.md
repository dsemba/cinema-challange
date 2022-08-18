# High Way Cinema

Our client has a cinema in Wrocław, Poland. Currently all movies schedule is done by Pen and Paper on big board where there is plan for given time for all movies the cinema shows. Planner Jadwiga need to schedule seans(seans is movie schedule at given time)for best used of the space.

## Board overview

![Cinema - page 1 copy](https://user-images.githubusercontent.com/34231627/150541482-0b1e4a66-4298-4d3e-846f-c62ba1c8e37b.png)


## Domain requirments

We would like to help Jadwiga to do better job with his weekly task with planning the seans. Idea is to create virtual board that she will be able to add seans to the board.

User Stories:
- Planner Jadwiga will be able to schedule Seans for given movie at particular time every day week from 8 - 22:00
- Any 2 scheduled movies can't be on same time and same room. Even the overlapping is forbidden.
- Every seans need to have maintenance slot to clean up whole Room. Every room have different cleaning slot.
- Some of the movies can have 3d glasses required.
- Not every movies are equal e.g. Premier need to be after working hours around 17:00-21:00
- There is possibilities that given room may not be available for particular time slot time or even days.


You task is to model the week planning of the seans by Jadwiga.

## Assumption
- Catalog of movies already exists(telling if it need 3d glasses, how long the movie will take)
- Room availibility is also implemented

### Challange notes

* Movie Catalog and Room Availibilty is not in scope of this challange but some model will be required to fulfill given task
* Consider concurrency modification. How to solve problem
when two Jadwiga's add different movies to same time and same room.
* If you have question to requirements simply just ask us.
* If during the assignment you will work on real database and UI you will lose precise time, so we encourage you to not do so.

#### What we care for:
- Solid domain model
- Quality of tests
- Clean code
- Proper module/context modeling

#### What we don’t care for:
- UI to be implemented
- Not require to use database
- All of the cases to be covered.

#### What we expect from solution:
- Treat it like production code. Develop your software in the same way that you would for any code that is intended to be deployed to production.
- Would be good to describe decision you make so future developers won't be scratching the head about the reasoning.
- Test should be green
- Code should be on github repo.


#### To clarify:
- does requirement for 3d glasses have any influence on how movies are schedules? or just a property?
- "Every room have different cleaning slot." meaning it takes a different amount of time to clean up each room?
- can there be no cleaning or 0 minutes maintenance after a seans?
- do 17-21 for scheduling premieres apply to the starting date or to the whole event?
- can the cleaning be done after opening hours? should it be scheduled for the next day?
- can the same movie be shown in different rooms at the same time?
