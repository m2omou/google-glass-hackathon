class Target < ActiveRecord::Base
  has_many :answers
  has_many :scores
end
