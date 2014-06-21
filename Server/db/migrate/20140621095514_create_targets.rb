class CreateTargets < ActiveRecord::Migration
  def change
    create_table :targets do |t|
      t.float :longitude
      t.float :latitude
      t.text :question
      t.text :answer
      t.text :fact
      t.string :picture

      t.timestamps
    end
  end
end
