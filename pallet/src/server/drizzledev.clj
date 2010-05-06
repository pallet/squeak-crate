(ns server.drizzledev
  (:require
   [pallet.resource.package :as package]
   [pallet.core :as core]
   [pallet.crate.automated-admin-user :as automated-admin-user]
   [pallet.crate.bzr :as bzr]
   [pallet.resource.service :as service]
   [pallet.resource.user :as user]
   [pallet.resource.hostinfo :as hostinfo]
   [pallet.crate.ssh-key :as ssh-key]))

(defn drizzledev-config
  []
  (user/user "testuser" :create-home true :shell :bash)
  (package/package-source
   "drizzle-developers"
   :aptitude {:url "ppa:drizzle-developers/ppa"}
   :yum { :url (str
                "http://5dollarwhitebox.org/repos/drizzle"
                (hostinfo/architecture))})
  (package/package-manager :update)
  (bzr/bzr)
  (package/package "drizzle-dev"))



(core/defnode drizzledev
  [:ubuntu :X86_64 :smallest
   :image-name-matches ".*"
   :os-description-matches "[^J]+10.04[^32]+"]
  :bootstrap [(automated-admin-user/automated-admin-user)
              (package/package-manager :update)]
  :configure [(drizzledev-config)])
