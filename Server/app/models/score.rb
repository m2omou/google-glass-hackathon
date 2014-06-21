class Score < ActiveRecord::Base
  belongs_to :answer
  belongs_to :target
end
