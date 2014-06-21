class CreateScores < ActiveRecord::Migration
  def change
    create_table :scores do |t|
      t.integer :target_id
      t.text :answer_id

      t.timestamps
    end
  end
end
