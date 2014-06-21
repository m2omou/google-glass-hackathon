class RemoveAnswerToTargets < ActiveRecord::Migration
  def change
    remove_column :targets, :answer, :text
  end
end
