(ns tic-tac-toe.core
  (:refer-clojure :exclude [==])
  ;*CLJSBUILD-REMOVE*;(:use-macros [cljs.core.logic.macros :only [defna matcha conda conde fresh project ==]])
  (:use [clojure.core.logic :only [defna matcha conda conde fresh project ==]]))

(defn not-nil? [m]
  (project [m] (== (nil? m) false)))

(defn check [s
             m1 m2 m3
             q1 q2 q3]
  (matcha [m1 m2 m3]
    ([m nil m] (conda [(not-nil? m) (== q2 s)]))
    ([nil m m] (conda [(not-nil? m) (== q1 s)]))
    ([m m nil] (conda [(not-nil? m) (== q3 s)]))))

(defna write [s m q]
  ([s nil q] (== q s)))

(defn play [q s
            m11 m12 m13
            m21 m22 m23
            m31 m32 m33]
  (fresh [q11 q12 q13
          q21 q22 q23
          q31 q32 q33]
    (== q [q11 q12 q13
           q21 q22 q23
           q31 q32 q33])
    (conda [(check s m21 m22 m23 q21 q22 q23)]
           [(check s m12 m22 m32 q12 q22 q32)]
           [(check s m11 m12 m13 q11 q12 q13)]
           [(check s m31 m32 m33 q31 q32 q33)]
           [(check s m11 m21 m31 q11 q21 q31)]
           [(check s m13 m23 m33 q13 q23 q33)]
           [(check s m11 m22 m33 q11 q22 q33)]
           [(check s m13 m22 m31 q13 q22 q31)]
           [(write s m22 q22)]
           [(write s m12 q12)]
           [(write s m21 q21)]
           [(write s m23 q23)]
           [(write s m32 q32)]
           [(write s m11 q11)]
           [(write s m13 q13)]
           [(write s m31 q31)]
           [(write s m33 q33)])))

(defna complete [b m1 m2 m3]
  ([b s s s] (== b s)))

(defn end [q s
           m11 m12 m13
           m21 m22 m23
           m31 m32 m33]
  (conde [(complete q m11 m12 m13)]
         [(complete q m21 m22 m23)]
         [(complete q m31 m32 m33)]
         [(complete q m11 m21 m31)]
         [(complete q m12 m22 m32)]
         [(complete q m13 m23 m33)]
         [(complete q m11 m22 m33)]
         [(complete q m13 m22 m31)]))
