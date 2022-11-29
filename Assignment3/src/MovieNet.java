/*
 * Six Degrees of Kevin Bacon
*/

import java.lang.*;
import java.util.*;

public class MovieNet {

  static final String KevinBacon = "Bacon, Kevin";
  HashSet<String> movies;
  HashSet<String> actors;
  //HashMap<String, Integer> distance;
  //HashMap<String, Integer> npath;

  HashMap<String, HashSet<String>> actorActorRelations; // {actor, {co-stared actors}}
  HashMap<String, HashSet<String>> movieActorRelations; // {movie, {actors}}
  HashMap<String, HashSet<String>> actorMovieRelations; // {actor, {movies}}

  // Each instance of movielines is String[] such that
  //	String[0] = title of movie
  //	String[1..n-1] = list of actors
  public MovieNet(LinkedList<String[]> movielines) {
    this.movies = new HashSet<>();
    this.actors = new HashSet<>();

    this.actorActorRelations = new HashMap<>();
    this.movieActorRelations = new HashMap<>();
    this.actorMovieRelations = new HashMap<>();

    // init movies, actors, movieActorRelations
    String currMovie = null;
    String currActor = null;
    for (String[] movieline: movielines) {
      currMovie = movieline[0];
      this.movies.add(currMovie);
      this.movieActorRelations.put(currMovie, new HashSet<>());

      for (int i=1; i<movieline.length; i++) {
        currActor = movieline[i];
        this.actors.add(currActor);
        this.movieActorRelations.get(currMovie).add(currActor);
      }
    }

    // init actorMovieRelations
    for (String actor: actors) {
      actorMovieRelations.put(actor, new HashSet<>());
    }
    for (String movie: movies) {
      for (String actor: movieActorRelations.get(movie)) {
        actorMovieRelations.get(actor).add(movie);
      }
    }

    // init actorActorRelations
    for (String actor: actors) {
      actorActorRelations.put(actor, new HashSet<>());
    }
    for (String movie: movies) {
      for (String actor: movieActorRelations.get(movie)) {
        actorActorRelations.get(actor).addAll(movieActorRelations.get(movie));
      }
    }

    /*this.distance = new HashMap<>();
    this.npath = new HashMap<>();

    for (String actor: this.actors) {
      distance.put(actor, Integer.MAX_VALUE); // inf
      npath.put(actor, 0);
    } */

    /* System.out.print("actors: ");
    for (String actor: actors) {
      System.out.print(actor+" ");
    }
    System.out.print("\nmovies: ");
    for (String movie: movies) {
      System.out.print(movie+" ");
    }
    System.out.print("\nactor-actor: ");
    for (String actor: actors) {
      System.out.print("Actor "+actor+": ");
      for (String coactor: actorActorRelations.get(actor)) {
        System.out.print(coactor+" ");
      }
      System.out.println();
    }
    System.out.print("\nactor-movie: ");
    for (String actor: actors) {
      System.out.print("Actor "+actor+": ");
      for (String movie: actorMovieRelations.get(actor)) {
        System.out.print(movie+" ");
      }
      System.out.println();
    }
    System.out.print("\nmovie-actor: ");
    for (String movie: movies) {
      System.out.print("Movie "+movie+": ");
      for (String actor: movieActorRelations.get(movie)) {
        System.out.print(actor+" ");
      }
      System.out.println();
    } */





    // init actorActorRelations
    /*for (String actor: actors) {
      actorActorRelations.put(actor, new HashSet<>());
    }
    for (String actor: actors) {
      for (String movie: movieActorRelations.keySet()){
        if (movieActorRelations.get(movie).contains(actor)) {
          actorActorRelations.get(actor).addAll(movieActorRelations.get(movie));
          // need to exclude himself?
        }
      }
    } */
  }	// Constructor

/*============================================================================*/

  // [Q1]
  public String[] moviesby(String[] actors) {
    HashSet<String> actorSet = new HashSet<>(Arrays.asList(actors));
    for (String actor: actorSet) {
      if (!this.actors.contains(actor)) return null;
    }

    HashSet<String> moviesby = new HashSet<>(this.actorMovieRelations.get(actors[0]));

    for (String actor: actorSet) { // iterating set is faster maybe?
      moviesby.retainAll(this.actorMovieRelations.get(actor));
    }
    if (moviesby.isEmpty()) return null;
    return moviesby.toArray(new String[0]);
  }

  // [Q2]
  public String[] castin(String[] titles) {
    HashSet<String> titleSet = new HashSet<>(Arrays.asList(titles));
    for (String movie: titleSet) {
      if (!this.movies.contains(movie)) return null;
    }

    HashSet<String> castin = new HashSet<>(this.movieActorRelations.get(titles[0]));
    for (String movie: titleSet) {
      castin.retainAll(this.movieActorRelations.get(movie));
    }
    if (castin.isEmpty()) return null;
    return castin.toArray(new String[0]);
  }

  // [Q3]
  public String[] pairmost(String[] actors) {
    //if (this.movieActorRelations.isEmpty()) return null;
    //if (this.actorMovieRelations.isEmpty()) return null;
    String[] maxPair = new String[2];
    int max = 0;
    for (int i=0; i<actors.length; i++) {
      if (!this.actors.contains(actors[i])) continue;
      for (int j=i+1; j<actors.length; j++) {
        if (!this.actors.contains(actors[j])) continue;
        String[] actorPair = new String[2];
        actorPair[0] = actors[i];
        actorPair[1] = actors[j];
        HashSet<String> tmpSet = new HashSet<>(this.actorMovieRelations.get(actorPair[0]));
        tmpSet.retainAll(this.actorMovieRelations.get(actorPair[1]));
        int pairCnt = tmpSet.size();
        if (pairCnt > max) {
          max = pairCnt;
          maxPair[0] = actorPair[0];
          maxPair[1] = actorPair[1];
        }
      }
    }
    if (max == 0) return null;
    else return maxPair;
  }

  // [Q4]
  public int Bacon(String actor) {
    return distance(actor, KevinBacon);
  }

  // [Q5]
  public int distance(String src, String dst) {
    // BFS with distance
    if (!actors.contains(src) || !actors.contains(dst)) return -1;
    if (src.equals(dst)) return 0;
    HashSet<String> visited = new HashSet<>();
    LinkedList<String> queue = new LinkedList<>();

    int distance = 0;
    int queueSize = 0;
    String current = src;
    // visit
    queue.offer(current);
    distance++;
    if (current.equals(dst)) return distance;
    visited.add(current);
    while (!queue.isEmpty()) {
      queueSize = queue.size();
      //System.out.print("distance: " + distance + "\n");
      for (int i=0; i<queueSize; i++) {
        current = queue.poll();
        for (String node: this.actorActorRelations.get(current)) {
          if (!visited.contains(node)) {
            queue.offer(node);
            if (node.equals(dst)) return distance;
            visited.add(node);
          }
        }
      }
      distance++;
    }
    return -1; // unreachable
  }

  // [Q6]
  public int npath(String src, String dst) {
    if (src.equals(dst)) return 1;
    if (!actors.contains(src) || !actors.contains(dst)) return 0;
    // TODO

    HashSet<String> visited = new HashSet<>(actors.size());
    LinkedList<String> queue = new LinkedList<>();

    HashMap<String, Integer> distance = new HashMap<>(actors.size());
    HashMap<String, Integer> npath = new HashMap<>(actors.size());

    //HashMap<String, int[]> info = new HashMap<>(actors.size());
    //[distance, npath]
    for (String actor: this.actors) {
      //int[] arr = new int[]{Integer.MAX_VALUE, 0};
      //info.put(actor, arr);

      distance.put(actor, Integer.MAX_VALUE);  // inf
      npath.put(actor, 0);

    }
    npath.put(src, 1);
    distance.put(src, 0);
    //info.get(src)[0] = 0; // distance
    //info.get(src)[1] = 1; // npath

    String current = src;
    queue.offer(current);
    visited.add(current);
    while (!queue.isEmpty()) {
      current = queue.poll();
      for (String node : this.actorActorRelations.get(current)) {
        // visit each node
        if (!visited.contains(node)) {
          queue.offer(node);
          visited.add(node);
        }
        // update distance & npath
        if (distance.get(node) > distance.get(current) + 1) {
        //if (info.get(node)[0] > info.get(current)[0] + 1) {
          // shorter path exists: src ----> current -> node
          distance.put(node, distance.get(current) + 1);
          npath.put(node, npath.get(current));
          //info.get(node)[0] = info.get(current)[0] + 1;
          //info.get(node)[1] = info.get(current)[1];
        }
        else if (distance.get(node) == distance.get(current) + 1) { // more paths with same distance
        //else if (info.get(node)[0] == info.get(current)[0] + 1) {
          npath.put(node, npath.get(node) + npath.get(current));
          //info.get(node)[1] += info.get(current)[1];
        }
      }
    }
    return npath.get(dst);
    //return info.get(dst)[1];
  }

  // [Q7]
  public String[] apath(String src, String dst) {
    if (src.equals(dst)) {
      return new String[]{src};
    }
    if (!actors.contains(src) || !actors.contains(dst)) return null;

    HashSet<String> visited = new HashSet<>();
    LinkedList<String> queue = new LinkedList<>();

    HashMap<String, Integer> distance = new HashMap<>(actors.size());
    //HashMap<String, Integer> npath = new HashMap<>(actors.size());
    HashMap<String, String> before = new HashMap<>(actors.size());

    for (String actor: this.actors) {
      //int[] value = {Integer.MAX_VALUE, 0};
      distance.put(actor, Integer.MAX_VALUE); // inf
      before.put(actor, null);
      //npath.put(actor, 0);
    }
    //npath.put(src, 1);
    distance.put(src, 0);

    String current = src;

    queue.offer(current);
    visited.add(current);
    //path.add(src);

    while (!queue.isEmpty()) {
      current = queue.poll();
      for (String node : this.actorActorRelations.get(current)) {
        // visit each node
        if (!visited.contains(node)) {
          queue.offer(node);
          visited.add(node);
        }
        // update distance & npath
        if (distance.get(node) > distance.get(current) + 1) {
          // shorter path exists: src ----> current -> node
          distance.put(node, distance.get(current) + 1);
          before.put(node, current);
          //npath.put(node, npath.get(current));
        }
        if (node.equals(dst)) break;
      }
    }
    if (before.get(dst)==null) return null;
    String path = dst;
    Stack<String> tmp = new Stack<>();

    int i=0;
    while (path != null) {
      tmp.add(path);
      path = before.get(path);
    }
    String[] apath = new String[tmp.size()];
    while (!tmp.isEmpty()) {
      apath[i++] = tmp.pop();
    }
    return apath;
  }

  // [Q8]
  public int eccentricity(String actor) {
    // similar with Q6
    HashSet<String> visited = new HashSet<>(actors.size());
    HashMap<String, Integer> distance = new HashMap<>(actors.size());
    LinkedList<String> queue = new LinkedList<>();

    for (String actor_: this.actors) {
      distance.put(actor_, Integer.MAX_VALUE); // inf
    }
    distance.put(actor, 0);

    String current = actor;
    queue.offer(current);
    visited.add(current);
    while (!queue.isEmpty()) {
      current = queue.poll();
      for (String node : this.actorActorRelations.get(current)) {
        if (!visited.contains(node)) {
          queue.offer(node);
          visited.add(node);
        }
        if (distance.get(node) > distance.get(current) + 1) {
          distance.put(node, distance.get(current) + 1);
        }
      }
    }

    distance.values().removeAll(Collections.singleton(Integer.MAX_VALUE));
    return Collections.max(distance.values());
  }

  // [Q9]
  public float closeness(String actor) {
    // similar with Q6
    HashSet<String> visited = new HashSet<>(actors.size());
    HashMap<String, Integer> distance = new HashMap<>(actors.size());
    LinkedList<String> queue = new LinkedList<>();

    for (String actor_: this.actors) {
      distance.put(actor_, Integer.MAX_VALUE);
    }
    distance.put(actor, 0);

    String current = actor;
    queue.offer(current);
    visited.add(current);
    while (!queue.isEmpty()) {
      current = queue.poll();
      for (String node : this.actorActorRelations.get(current)) {
        if (!visited.contains(node)) {
          queue.offer(node);
          visited.add(node);
        }
        if (distance.get(node) > distance.get(current) + 1) {
          distance.put(node, distance.get(current) + 1);
        }
      }
    }

    distance.values().removeAll(Collections.singleton(Integer.MAX_VALUE));
    distance.remove(actor);
    float closeness = 0.0F;
    for (int dist: distance.values()) {
      closeness += 1.0F / Math.pow(2, dist);
    }
    return closeness;
  }



/*============================================================================*/

}

