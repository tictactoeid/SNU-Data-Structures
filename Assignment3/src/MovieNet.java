/*
 * Six Degrees of Kevin Bacon
*/

import java.lang.*;
import java.util.*;

public class MovieNet {

  static final String KevinBacon = "Bacon, Kevin";
  HashSet<String> movies;
  HashSet<String> actors;

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
    HashSet<String> moviesby = new HashSet<>(this.actorMovieRelations.get(actors[0]));
    Set<String> actorSet = Set.of(actors);
    /*for (String movie: this.movies) {
      for (String actor: actors) {
        if (!this.movieActorRelations.get(movie).contains(actor)) {
          continue;
        }
        moviesby.add(movie);
      }
    }*/
    for (String actor: actorSet) { // iterating set is faster maybe?
      moviesby.retainAll(this.actorMovieRelations.get(actor));
    }
    if (moviesby.isEmpty()) return null;
    return moviesby.toArray(new String[0]);
  }

  // [Q2]
  public String[] castin(String[] titles) {
    HashSet<String> castin = new HashSet<>(this.movieActorRelations.get(titles[0]));
    Set<String> titleSet = Set.of(titles);
    for (String movie: titleSet) {
      castin.retainAll(this.movieActorRelations.get(movie));
    }
    if (castin.isEmpty()) return null;
    return castin.toArray(new String[0]);
  }

  // [Q3]
  public String[] pairmost(String[] actors) {
    String[] maxPair = new String[2];
    int max = 0;
    for (int i=0; i<actors.length; i++) {
      for (int j=i+1; j<actors.length; j++) {
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
  public int npath(String src, String dst) { // it may be works but tooooooo slow
    if (src.equals(dst)) return 1;
    if (!actors.contains(src) || !actors.contains(dst)) return 0;
    // TODO
    /*ArrayList<Integer> distance = new ArrayList<>();
    ArrayList<Integer> npath = new ArrayList<>();
    for (int i=0; i<actors.size(); i++) {
      distance.add(i, 0);
      npath.add(i, 0);
    }
    ArrayList<String> nodes = new ArrayList<>(actors);
    HashSet<String> visited = new HashSet<>();
    LinkedList<String> queue = new LinkedList<>();

    String current;
    int currDist, nodeDist;
    int currIdx, nodeIdx;
    distance.set(nodes.indexOf(src), 0);
    npath.set(nodes.indexOf(src), 1);
    queue.offer(src);
    visited.add(src);

    while (!queue.isEmpty()) {
      current = queue.poll();
      currIdx = nodes.indexOf(current);
      currDist = distance.get(currIdx);

      for (String node: this.actorActorRelations.get(current)) {
        nodeIdx = nodes.indexOf(node);
        nodeDist = distance.get(nodeIdx);
        if (!visited.contains(node)) { // visit
          queue.offer(node);
          visited.add(node);
          if (nodeDist == currDist + 1) {
            npath.set(nodeIdx, npath.get(nodeIdx) + npath.get(currIdx));
          }
          else if (nodeDist > currDist + 1) { // update distance and npath
            distance.set(nodeIdx, currDist+1);
            npath.set(nodeIdx, npath.get(currIdx));
          }
        }
        if (nodeDist == currDist + 1) {
          npath.set(nodeIdx, npath.get(nodeIdx) + npath.get(currIdx));
        }
        else if (nodeDist > currDist + 1) { // update distance and npath
          distance.set(nodeIdx, currDist+1);
          npath.set(nodeIdx, npath.get(currIdx));
        }
      }
    }
    return npath.get(nodes.indexOf(dst));*/
    int n = actors.size();
    //String[] actorsArray = this.actors.toArray(new String[0]);
    ArrayList<String> actorsList = new ArrayList<>(this.actors);
    boolean[] visited = new boolean[n];
    int[] distance = new int[n];
    int[] npath = new int[n];
    for (int i=0; i<n; i++) {
      visited[i] = false;
      npath[i] = 0;
      distance[i] = Integer.MAX_VALUE; // inf
    }
    int srcIdx = actorsList.indexOf(src);
    npath[srcIdx] = 1;
    distance[srcIdx] = 1;
    visited[srcIdx] = true;

    LinkedList<Integer> queue = new LinkedList<>();
    queue.offer(srcIdx);
    int current, nodeIdx;
    while (!queue.isEmpty()) {
      current = queue.poll();
      for (String node: this.actorActorRelations.get(actorsList.get(current))) {
        nodeIdx = actorsList.indexOf(node);
        if (!visited[nodeIdx]) { // visit
          queue.offer(nodeIdx);
          visited[nodeIdx] = true;
        }
        // update distance & npath
        if (distance[nodeIdx] > distance[current]+1) { // shorter path exists
          distance[nodeIdx] = distance[current] + 1;
          npath[nodeIdx] = npath[current];
        }
        else if (distance[nodeIdx] == distance[current]+1) { // more paths with same distance
          npath[nodeIdx] += npath[current];
        }
      }
    }
    return npath[actorsList.indexOf(dst)];

  }

  // [Q7]
  public String[] apath(String src, String dst) {


    if (src.equals(dst)) {
      return new String[]{src};
    }
    if (!actors.contains(src) || !actors.contains(dst)) return null;
    // TODO
    return null;
  }

  // [Q8]
  public int eccentricity(String actor) {
    return 0;
  }

  // [Q9]
  public float closeness(String actor) {
    return 0.0F;
  }

/*============================================================================*/

}

