class Answer < ActiveRecord::Base
  belongs_to :target

  # overwrite the as_json method to add avatar and thumb
  def as_json(options={})
    hash = super(except)
    hash
  end

  # hide certain information
  def except
    { :except=>  [:updated_at, :created_at] }
  end
end
